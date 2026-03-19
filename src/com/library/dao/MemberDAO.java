package com.library.dao;

import com.library.db.DatabaseConnection;
import com.library.model.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    private Connection conn;

    public MemberDAO() {
        conn = DatabaseConnection.getInstance().getConnection();
    }

    public void addMember(Member member) {
        try {
            String sql = "INSERT INTO members (name, email, phone, member_type, join_date) VALUES (?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPhone());
            ps.setString(4, member.getMemberType());
            ps.setDate(5, Date.valueOf(member.getJoinDate()));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding member: " + e.getMessage());
        }
    }

    public void updateMember(Member member) {
        try {
            String sql = "UPDATE members SET name=?, email=?, phone=?, member_type=? WHERE member_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPhone());
            ps.setString(4, member.getMemberType());
            ps.setInt(5, member.getMemberId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating member: " + e.getMessage());
        }
    }

    public void deleteMember(int memberId) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM members WHERE member_id=?");
            ps.setInt(1, memberId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting member: " + e.getMessage());
        }
    }

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM members");
            while (rs.next()) {
                members.add(new Member(
                        rs.getInt("member_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("member_type"),
                        rs.getDate("join_date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching members: " + e.getMessage());
        }
        return members;
    }

    public Member getMemberById(int memberId) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM members WHERE member_id=?");
            ps.setInt(1, memberId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Member(
                        rs.getInt("member_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("member_type"),
                        rs.getDate("join_date").toLocalDate()
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching member: " + e.getMessage());
        }
        return null;
    }

    public List<Member> searchMembers(String keyword) {
        List<Member> members = new ArrayList<>();
        try {
            String sql = "SELECT * FROM members WHERE name LIKE ? OR email LIKE ? OR member_type LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                members.add(new Member(
                        rs.getInt("member_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("member_type"),
                        rs.getDate("join_date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error searching members: " + e.getMessage());
        }
        return members;
    }

    public int getTotalMembers() {
        try {
            ResultSet rs = conn.createStatement()
                    .executeQuery("SELECT COUNT(*) FROM members");
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }
}