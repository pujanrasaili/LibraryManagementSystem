package com.library.model;

import java.time.LocalDate;

public class Member {
    private int memberId;
    private String name;
    private String email;
    private String phone;
    private String memberType;
    private LocalDate joinDate;

    public Member() {}

    public Member(int memberId, String name, String email,
                  String phone, String memberType, LocalDate joinDate) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.memberType = memberType;
        this.joinDate = joinDate;
    }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getMemberType() { return memberType; }
    public void setMemberType(String memberType) { this.memberType = memberType; }
    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }

    public int getLoanDays() {
        return switch (memberType) {
            case "Faculty" -> 30;
            case "Public"  -> 7;
            default        -> 14; // Student
        };
    }

    public double getFinePerDay() {
        return switch (memberType) {
            case "Faculty" -> 3.0;
            case "Public"  -> 10.0;
            default        -> 5.0; // Student
        };
    }

    @Override
    public String toString() { return name + " (" + memberType + ")"; }
}