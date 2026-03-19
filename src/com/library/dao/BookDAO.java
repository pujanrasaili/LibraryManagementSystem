package com.library.dao;

import com.library.model.Book;
import com.library.db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private Connection conn;

    public BookDAO() {
        conn = DatabaseConnection.getInstance().getConnection();
    }

    public void addBook(Book book) {
        try {
            String sql = "INSERT INTO books (isbn, title, author, category, total_copies, available_copies) VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, book.getIsbn());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setString(4, book.getCategory());
            ps.setInt(5, book.getTotalCopies());
            ps.setInt(6, book.getAvailableCopies());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding book: " + e.getMessage());
        }
    }

    public void updateBook(Book book) {
        try {
            String sql = "UPDATE books SET isbn=?, title=?, author=?, category=?, total_copies=?, available_copies=? WHERE book_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, book.getIsbn());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setString(4, book.getCategory());
            ps.setInt(5, book.getTotalCopies());
            ps.setInt(6, book.getAvailableCopies());
            ps.setInt(7, book.getBookId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating book: " + e.getMessage());
        }
    }

    public void deleteBook(int bookId) {
        try {
            String sql = "DELETE FROM books WHERE book_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bookId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting book: " + e.getMessage());
        }
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM books");
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("book_id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getInt("total_copies"),
                        rs.getInt("available_copies")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching books: " + e.getMessage());
        }
        return books;
    }

    public List<Book> searchBooks(String keyword, String filter) {
        List<Book> books = new ArrayList<>();
        try {
            String col = switch (filter) {
                case "Title"    -> "title";
                case "Author"   -> "author";
                case "ISBN"     -> "isbn";
                case "Category" -> "category";
                default         -> "title";
            };
            String sql = "SELECT * FROM books WHERE " + col + " LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("book_id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getInt("total_copies"),
                        rs.getInt("available_copies")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error searching books: " + e.getMessage());
        }
        return books;
    }

    public void updateAvailableCopies(int bookId, int copies) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE books SET available_copies=? WHERE book_id=?");
            ps.setInt(1, copies);
            ps.setInt(2, bookId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating copies: " + e.getMessage());
        }
    }

    public Book getBookById(int bookId) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM books WHERE book_id=?");
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Book(
                        rs.getInt("book_id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getInt("total_copies"),
                        rs.getInt("available_copies")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching book: " + e.getMessage());
        }
        return null;
    }

    public int getTotalBooks() {
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM books");
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }
}