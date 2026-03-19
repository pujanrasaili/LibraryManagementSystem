package com.library.dao;

import com.library.model.Book;
import com.library.model.Member;
import com.library.model.Transaction;
import com.library.model.Fine;
import com.library.db.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private Connection conn;

    public TransactionDAO() {
        conn = DatabaseConnection.getInstance().getConnection();
    }

    public boolean issueBook(int bookId, int memberId) {
        try {
            // Get member type for loan days
            Member member = new MemberDAO().getMemberById(memberId);
            Book book = new BookDAO().getBookById(bookId);

            if (member == null) {
                System.out.println("com.library.model.Member not found.");
                return false;
            }
            if (book == null || book.getAvailableCopies() <= 0) {
                System.out.println("com.library.model.Book not available.");
                return false;
            }

            LocalDate borrowDate = LocalDate.now();
            LocalDate dueDate = borrowDate.plusDays(member.getLoanDays());

            String sql = "INSERT INTO transactions (book_id, member_id, borrow_date, due_date, status) VALUES (?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bookId);
            ps.setInt(2, memberId);
            ps.setDate(3, Date.valueOf(borrowDate));
            ps.setDate(4, Date.valueOf(dueDate));
            ps.setString(5, "BORROWED");
            ps.executeUpdate();

            // Update available copies
            new BookDAO().updateAvailableCopies(bookId, book.getAvailableCopies() - 1);
            return true;

        } catch (SQLException e) {
            System.out.println("Error issuing book: " + e.getMessage());
            return false;
        }
    }

    public double returnBook(int transactionId) {
        try {
            // Get transaction details
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT t.*, m.member_type FROM transactions t " +
                            "JOIN members m ON t.member_id = m.member_id " +
                            "WHERE t.transaction_id=?");
            ps.setInt(1, transactionId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return -1;

            LocalDate dueDate    = rs.getDate("due_date").toLocalDate();
            int bookId           = rs.getInt("book_id");
            String memberType    = rs.getString("member_type");
            LocalDate returnDate = LocalDate.now();

            // Calculate fine
            double fine = 0;
            double finePerDay = switch (memberType) {
                case "Faculty" -> 3.0;
                case "Public"  -> 10.0;
                default        -> 5.0;
            };

            if (returnDate.isAfter(dueDate)) {
                long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
                fine = daysLate * finePerDay;
            }

            // Update transaction
            PreparedStatement update = conn.prepareStatement(
                    "UPDATE transactions SET return_date=?, status='RETURNED' WHERE transaction_id=?");
            update.setDate(1, Date.valueOf(returnDate));
            update.setInt(2, transactionId);
            update.executeUpdate();

            // Update book copies
            Book book = new BookDAO().getBookById(bookId);
            if (book != null) {
                new BookDAO().updateAvailableCopies(bookId, book.getAvailableCopies() + 1);
            }

            // Save fine if any
            if (fine > 0) {
                PreparedStatement finePs = conn.prepareStatement(
                        "INSERT INTO fines (transaction_id, amount, paid) VALUES (?,?,?)");
                finePs.setInt(1, transactionId);
                finePs.setDouble(2, fine);
                finePs.setString(3, "false");
                finePs.executeUpdate();
            }

            return fine;

        } catch (SQLException e) {
            System.out.println("Error returning book: " + e.getMessage());
            return -1;
        }
    }

    public List<Transaction> getActiveTransactions() {
        List<Transaction> list = new ArrayList<>();
        try {
            String sql =
                    "SELECT t.*, b.title, m.name FROM transactions t " +
                            "JOIN books b ON t.book_id = b.book_id " +
                            "JOIN members m ON t.member_id = m.member_id " +
                            "WHERE t.status = 'BORROWED' " +
                            "ORDER BY t.due_date ASC";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                list.add(new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getString("title"),
                        rs.getString("name"),
                        rs.getDate("borrow_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate(),
                        null,
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching transactions: " + e.getMessage());
        }
        return list;
    }

    public List<Transaction> getOverdueTransactions() {
        List<Transaction> list = new ArrayList<>();
        try {
            String sql =
                    "SELECT t.*, b.title, m.name FROM transactions t " +
                            "JOIN books b ON t.book_id = b.book_id " +
                            "JOIN members m ON t.member_id = m.member_id " +
                            "WHERE t.status = 'BORROWED' AND t.due_date < CURDATE() " +
                            "ORDER BY t.due_date ASC";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                list.add(new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getString("title"),
                        rs.getString("name"),
                        rs.getDate("borrow_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate(),
                        null,
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching overdue: " + e.getMessage());
        }
        return list;
    }

    public List<Transaction> getMemberHistory(int memberId) {
        List<Transaction> list = new ArrayList<>();
        try {
            String sql =
                    "SELECT t.*, b.title, m.name FROM transactions t " +
                            "JOIN books b ON t.book_id = b.book_id " +
                            "JOIN members m ON t.member_id = m.member_id " +
                            "WHERE t.member_id=? ORDER BY t.borrow_date DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, memberId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getString("title"),
                        rs.getString("name"),
                        rs.getDate("borrow_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getDate("return_date") != null ?
                                rs.getDate("return_date").toLocalDate() : null,
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching history: " + e.getMessage());
        }
        return list;
    }

    public List<Fine> getUnpaidFines() {
        List<Fine> list = new ArrayList<>();
        try {
            String sql =
                    "SELECT f.*, m.name, b.title FROM fines f " +
                            "JOIN transactions t ON f.transaction_id = t.transaction_id " +
                            "JOIN members m ON t.member_id = m.member_id " +
                            "JOIN books b ON t.book_id = b.book_id " +
                            "WHERE f.paid = 'false'";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                list.add(new Fine(
                        rs.getInt("fine_id"),
                        rs.getInt("transaction_id"),
                        rs.getString("name"),
                        rs.getString("title"),
                        rs.getDouble("amount"),
                        rs.getString("paid")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching fines: " + e.getMessage());
        }
        return list;
    }

    public void markFinePaid(int fineId) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE fines SET paid='true' WHERE fine_id=?");
            ps.setInt(1, fineId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error marking fine paid: " + e.getMessage());
        }
    }

    public int getActiveBorrowingsCount() {
        try {
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM transactions WHERE status='BORROWED'");
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }

    public int getOverdueCount() {
        try {
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM transactions WHERE status='BORROWED' AND due_date < CURDATE()");
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }
}