package com.library.controller;

import com.library.dao.BookDAO;
import com.library.dao.MemberDAO;
import com.library.dao.TransactionDAO;
import com.library.model.Book;
import com.library.model.Fine;
import com.library.model.Member;
import com.library.model.Transaction;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.time.LocalDate;

public class Main extends Application {

    private Stage primaryStage;
    private final BookDAO bookDAO = new BookDAO();
    private final MemberDAO memberDAO = new MemberDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "library123";
    private Button activeSidebarBtn = null;

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Digital Library Management System");
        stage.setMaximized(true);
        showLoginScreen();
    }

    private void showLoginScreen() {
        VBox leftPanel = new VBox(20);
        leftPanel.setMinWidth(420);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setPadding(new Insets(60));
        leftPanel.setStyle("-fx-background-color: linear-gradient(to bottom right, #0D1B6E, #1a237e, #1E3A8A);");

        Label icon = new Label("📚");
        icon.setFont(Font.font("Segoe UI Emoji", 72));
        Label appName = new Label("LibraryPro");
        appName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 36));
        appName.setTextFill(Color.WHITE);
        Label appSub = new Label("Digital Library Management System");
        appSub.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 15));
        appSub.setTextFill(Color.web("#93C5FD"));
        Separator sep = new Separator();
        sep.setMaxWidth(260);
        Label desc = new Label("A complete solution for managing books,\nmembers, borrowing & fine collections.");
        desc.setFont(Font.font("Segoe UI", 13));
        desc.setTextFill(Color.web("#BFDBFE"));
        desc.setTextAlignment(TextAlignment.CENTER);
        desc.setAlignment(Pos.CENTER);
        HBox pills1 = new HBox(10, pill("📖 Books"), pill("👥 Members"), pill("🔄 Borrowing"));
        pills1.setAlignment(Pos.CENTER);
        HBox pills2 = new HBox(10, pill("💰 Fines"), pill("📊 Dashboard"), pill("📋 Reports"));
        pills2.setAlignment(Pos.CENTER);
        Label uni = new Label("University of Bedfordshire  •  CIS096-1");
        uni.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 11));
        uni.setTextFill(Color.web("#6B7FD4"));
        leftPanel.getChildren().addAll(icon, appName, appSub, sep, desc, pills1, pills2, uni);

        VBox rightPanel = new VBox(0);
        rightPanel.setMinWidth(450);
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setStyle("-fx-background-color: #F8FAFF;");

        VBox formBox = new VBox(14);
        formBox.setMaxWidth(380);

        Label loginTitle = new Label("Welcome Back 👋");
        loginTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        loginTitle.setTextFill(Color.web("#0D1B6E"));
        Label loginSub = new Label("Sign in to your librarian account");
        loginSub.setFont(Font.font("Segoe UI", 14));
        loginSub.setTextFill(Color.web("#6B7280"));

        Label userLbl = fieldLabel("USERNAME");
        TextField usernameField = loginTextField("Enter your username");
        Label passLbl = fieldLabel("PASSWORD");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefHeight(48);
        passwordField.setMaxWidth(Double.MAX_VALUE);
        passwordField.setStyle(inputStyle());

        Label errorLbl = new Label("");
        errorLbl.setFont(Font.font("Segoe UI", 12));
        errorLbl.setTextFill(Color.web("#DC2626"));

        Button loginBtn = new Button("SIGN IN  →");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setPrefHeight(50);
        loginBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        loginBtn.setStyle("-fx-background-color:#1a237e;-fx-text-fill:white;-fx-background-radius:10;-fx-cursor:hand;");
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle("-fx-background-color:#0D1B6E;-fx-text-fill:white;-fx-background-radius:10;-fx-cursor:hand;"));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle("-fx-background-color:#1a237e;-fx-text-fill:white;-fx-background-radius:10;-fx-cursor:hand;"));

        Label hint = new Label("🔑  Default:  admin  /  library123");
        hint.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 12));
        hint.setTextFill(Color.web("#9CA3AF"));

        Runnable doLogin = () -> {
            if (usernameField.getText().trim().equals(ADMIN_USER) &&
                    passwordField.getText().trim().equals(ADMIN_PASS)) {
                showMainApp();
            } else {
                errorLbl.setText("❌  Invalid username or password.");
                passwordField.clear();
                usernameField.setStyle(inputStyle().replace("#C7D2FE", "#FCA5A5"));
                passwordField.setStyle(inputStyle().replace("#C7D2FE", "#FCA5A5"));
            }
        };
        loginBtn.setOnAction(e -> doLogin.run());
        passwordField.setOnAction(e -> doLogin.run());
        usernameField.setOnKeyTyped(e -> {
            usernameField.setStyle(inputStyle());
            passwordField.setStyle(inputStyle());
            errorLbl.setText("");
        });

        formBox.getChildren().addAll(loginTitle, loginSub,
                new Region() {{ setPrefHeight(8); }},
                userLbl, usernameField, passLbl, passwordField,
                errorLbl, loginBtn, hint);

        rightPanel.getChildren().add(formBox);

        HBox root = new HBox();
        HBox.setHgrow(leftPanel, Priority.ALWAYS);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);
        root.getChildren().addAll(leftPanel, rightPanel);

        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
        javafx.application.Platform.runLater(() -> primaryStage.setMaximized(true));
    }

    private void showMainApp() {
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(230);
        sidebar.setStyle("-fx-background-color: #0D1B6E;");

        VBox sideHeader = new VBox(4);
        sideHeader.setPadding(new Insets(22, 16, 22, 20));
        sideHeader.setStyle("-fx-background-color: #091460;");
        Label sideIcon = new Label("📚");
        sideIcon.setFont(Font.font("Segoe UI Emoji", 30));
        Label sideTitle = new Label("LibraryPro");
        sideTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        sideTitle.setTextFill(Color.WHITE);
        Label sideRole = new Label("Librarian Dashboard");
        sideRole.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 12));
        sideRole.setTextFill(Color.web("#93C5FD"));
        sideHeader.getChildren().addAll(sideIcon, sideTitle, sideRole);

        VBox nav = new VBox(3);
        nav.setPadding(new Insets(16, 10, 10, 10));
        Button dashBtn    = sidebarBtn("🏠", "Dashboard");
        Button booksBtn   = sidebarBtn("📖", "Books");
        Button membersBtn = sidebarBtn("👥", "Members");
        Button issueBtn   = sidebarBtn("📤", "Issue Book");
        Button returnBtn  = sidebarBtn("📥", "Return Book");
        Button finesBtn   = sidebarBtn("💰", "Fines");
        Button reportsBtn = sidebarBtn("📋", "Reports");
        nav.getChildren().addAll(
                navSection("MAIN MENU"), dashBtn, booksBtn, membersBtn,
                navSection("CIRCULATION"), issueBtn, returnBtn,
                navSection("MANAGEMENT"), finesBtn, reportsBtn);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox userCard = new VBox(6);
        userCard.setPadding(new Insets(14));
        userCard.setStyle("-fx-background-color:#091460;-fx-border-color:#1E3A8A;-fx-border-width:1 0 0 0;");
        Label userLbl = new Label("👤  Librarian Admin");
        userLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        userLbl.setTextFill(Color.WHITE);
        Button logoutBtn = new Button("⬅  Sign Out");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setPrefHeight(38);
        logoutBtn.setStyle("-fx-background-color:#DC2626;-fx-text-fill:white;-fx-font-size:13px;-fx-font-weight:bold;-fx-background-radius:8;-fx-cursor:hand;");
        logoutBtn.setOnAction(e -> showLoginScreen());
        userCard.getChildren().addAll(userLbl, logoutBtn);
        sidebar.getChildren().addAll(sideHeader, nav, spacer, userCard);

        HBox topBar = new HBox();
        topBar.setPrefHeight(58);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 24, 0, 24));
        topBar.setStyle("-fx-background-color:white;-fx-border-color:#E2E8F0;-fx-border-width:0 0 1 0;-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.05),4,0,0,2);");
        Label pageTitle = new Label("Dashboard");
        pageTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        pageTitle.setTextFill(Color.web("#0D1B6E"));
        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);
        Label dateLbl = new Label("📅  " + LocalDate.now());
        dateLbl.setFont(Font.font("Segoe UI", 13));
        dateLbl.setTextFill(Color.web("#6B7280"));
        Label adminBadge = new Label("  👤  Admin  ");
        adminBadge.setStyle("-fx-background-color:#EEF2FF;-fx-text-fill:#1a237e;-fx-font-weight:bold;-fx-font-size:12px;-fx-background-radius:20;-fx-padding:5 14;");
        topBar.getChildren().addAll(pageTitle, topSpacer, dateLbl, new Label("   "), adminBadge);

        StackPane content = new StackPane();
        content.setStyle("-fx-background-color:#F1F5F9;");

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setTop(topBar);
        root.setCenter(content);

        dashBtn.setOnAction(e -> { setActive(dashBtn); pageTitle.setText("Dashboard"); content.getChildren().setAll(buildDashboard()); });
        booksBtn.setOnAction(e -> { setActive(booksBtn); pageTitle.setText("Book Management"); content.getChildren().setAll(buildBooksPage()); });
        membersBtn.setOnAction(e -> { setActive(membersBtn); pageTitle.setText("Member Management"); content.getChildren().setAll(buildMembersPage()); });
        issueBtn.setOnAction(e -> { setActive(issueBtn); pageTitle.setText("Issue Book"); content.getChildren().setAll(buildIssuePage()); });
        returnBtn.setOnAction(e -> { setActive(returnBtn); pageTitle.setText("Return Book"); content.getChildren().setAll(buildReturnPage()); });
        finesBtn.setOnAction(e -> { setActive(finesBtn); pageTitle.setText("Fine Management"); content.getChildren().setAll(buildFinesPage()); });
        reportsBtn.setOnAction(e -> { setActive(reportsBtn); pageTitle.setText("Reports"); content.getChildren().setAll(buildReportsPage()); });

        setActive(dashBtn);
        content.getChildren().setAll(buildDashboard());

        Scene scene = new Scene(root, 1200, 750);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
    }

    private ScrollPane buildDashboard() {
        int totalBooks   = bookDAO.getTotalBooks();
        int totalMembers = memberDAO.getTotalMembers();
        int activeTx     = transactionDAO.getActiveBorrowingsCount();
        int overdue      = transactionDAO.getOverdueCount();

        HBox cards = new HBox(16,
                statCard("📚", "Total Books",       String.valueOf(totalBooks),   "#1D4ED8", "#EFF6FF"),
                statCard("👥", "Total Members",     String.valueOf(totalMembers), "#15803D", "#F0FDF4"),
                statCard("🔄", "Active Borrowings", String.valueOf(activeTx),     "#B45309", "#FFFBEB"),
                statCard("⚠",  "Overdue Books",     String.valueOf(overdue),      "#B91C1C", "#FEF2F2"));

        VBox alertBox = new VBox();
        if (overdue > 0) {
            HBox alert = new HBox(12);
            alert.setPadding(new Insets(14, 18, 14, 18));
            alert.setAlignment(Pos.CENTER_LEFT);
            alert.setStyle("-fx-background-color:#FEF2F2;-fx-border-color:#FECACA;-fx-border-radius:10;-fx-background-radius:10;");
            Label alertIcon = new Label("🚨"); alertIcon.setFont(Font.font(16));
            Label alertText = new Label(overdue + " overdue book(s) detected! Please follow up with members immediately.");
            alertText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
            alertText.setTextFill(Color.web("#991B1B"));
            alert.getChildren().addAll(alertIcon, alertText);
            alertBox.getChildren().add(alert);
        }

        HBox policyRow = new HBox(16,
                policyCard("🎓", "Student",  "14 days loan", "£5 / day fine",  "#1D4ED8"),
                policyCard("👨‍🏫", "Faculty", "30 days loan", "£3 / day fine",  "#15803D"),
                policyCard("🏛",  "Public",  "7 days loan",  "£10 / day fine", "#B45309"));

        Label recentTitle = new Label("📋  Recent Active Borrowings");
        recentTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        recentTitle.setTextFill(Color.web("#0D1B6E"));

        TableView<Transaction> table = new TableView<>();
        table.setStyle("-fx-background-color:white;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(260);

        TableColumn<Transaction,Integer> txIdCol = new TableColumn<>("TX ID");
        txIdCol.setCellValueFactory(new PropertyValueFactory<>("transactionId")); txIdCol.setPrefWidth(70);
        TableColumn<Transaction,String> bookCol = new TableColumn<>("Book Title");
        bookCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        TableColumn<Transaction,String> memberCol = new TableColumn<>("Member");
        memberCol.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        TableColumn<Transaction,String> borrowCol = new TableColumn<>("Borrow Date");
        borrowCol.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        TableColumn<Transaction,String> dueCol = new TableColumn<>("Due Date");
        dueCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        table.getColumns().addAll(txIdCol, bookCol, memberCol, borrowCol, dueCol, statusCol());
        table.setItems(FXCollections.observableArrayList(transactionDAO.getActiveTransactions()));

        VBox layout = new VBox(16, cards, alertBox, policyRow, recentTitle, table);
        layout.setPadding(new Insets(24));
        layout.setStyle("-fx-background-color:#F1F5F9;");
        ScrollPane sp = new ScrollPane(layout);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:#F1F5F9;-fx-background:#F1F5F9;");
        return sp;
    }

    private ScrollPane buildBooksPage() {
        Label formTitle = sectionHeader("➕  Add New Book");
        TextField isbnF  = inputField("ISBN");
        TextField titleF = inputField("Title");
        TextField authF  = inputField("Author");
        TextField catF   = inputField("Category");
        TextField copF   = inputField("Copies");

        Button addBtn = actionButton("➕  Add Book", "#15803D");
        Label status  = new Label("");
        status.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));

        GridPane form = new GridPane();
        form.setHgap(14); form.setVgap(14);
        form.add(fLbl("ISBN:"),     0, 0); form.add(isbnF,  1, 0);
        form.add(fLbl("Title:"),    2, 0); form.add(titleF, 3, 0);
        form.add(fLbl("Author:"),   0, 1); form.add(authF,  1, 1);
        form.add(fLbl("Category:"), 2, 1); form.add(catF,   3, 1);
        form.add(fLbl("Copies:"),   0, 2); form.add(copF,   1, 2);
        form.add(addBtn,            3, 2);

        VBox formCard = pageCard(formTitle, form, status);

        TextField searchF = inputField("🔍  Search books...");
        searchF.setPrefWidth(280);
        ComboBox<String> filterBox = new ComboBox<>();
        filterBox.getItems().addAll("Title", "Author", "ISBN", "Category");
        filterBox.setValue("Title");
        filterBox.setStyle(comboStyle());
        filterBox.setPrefHeight(40);

        Button searchBtn  = actionButton("Search",    "#1D4ED8");
        Button showAllBtn = actionButton("Show All",  "#7C3AED");
        Button editBtn    = actionButton("✏  Edit",   "#B45309");
        Button deleteBtn  = actionButton("🗑  Delete", "#DC2626");

        HBox searchBar = new HBox(10, searchF, filterBox, searchBtn, showAllBtn, editBtn, deleteBtn);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        TableView<Book> table = new TableView<>();
        table.setStyle("-fx-background-color:white;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<Book,Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("bookId")); idCol.setPrefWidth(55);
        TableColumn<Book,String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        TableColumn<Book,String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Book,String> authCol = new TableColumn<>("Author");
        authCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        TableColumn<Book,String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<Book,Integer> totCol = new TableColumn<>("Total");
        totCol.setCellValueFactory(new PropertyValueFactory<>("totalCopies")); totCol.setPrefWidth(65);
        TableColumn<Book,Integer> avCol = new TableColumn<>("Available");
        avCol.setCellValueFactory(new PropertyValueFactory<>("availableCopies")); avCol.setPrefWidth(85);
        table.getColumns().addAll(idCol, isbnCol, titleCol, authCol, catCol, totCol, avCol);
        refreshBooks(table);

        addBtn.setOnAction(e -> {
            try {
                bookDAO.addBook(new Book(0, isbnF.getText().trim(), titleF.getText().trim(),
                        authF.getText().trim(), catF.getText().trim(),
                        Integer.parseInt(copF.getText().trim()),
                        Integer.parseInt(copF.getText().trim())));
                setStatus(status, "✅  Book added successfully!", true);
                isbnF.clear(); titleF.clear(); authF.clear(); catF.clear(); copF.clear();
                addBtn.setText("➕  Add Book");
                addBtn.setStyle("-fx-background-color:#15803D;-fx-text-fill:white;-fx-font-size:13px;-fx-font-weight:bold;-fx-background-radius:8;-fx-cursor:hand;-fx-padding:0 18px;");
                refreshBooks(table);
            } catch (Exception ex) {
                setStatus(status, "⚠  Fill all fields correctly.", false);
            }
        });

        editBtn.setOnAction(e -> {
            Book sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                isbnF.setText(sel.getIsbn());
                titleF.setText(sel.getTitle());
                authF.setText(sel.getAuthor());
                catF.setText(sel.getCategory());
                copF.setText(String.valueOf(sel.getTotalCopies()));
                setStatus(status, "✏  Editing: " + sel.getTitle() + " — modify fields and click Update.", true);
                addBtn.setText("💾  Update Book");
                addBtn.setStyle("-fx-background-color:#B45309;-fx-text-fill:white;-fx-font-size:13px;-fx-font-weight:bold;-fx-background-radius:8;-fx-cursor:hand;-fx-padding:0 18px;");
                addBtn.setOnAction(ev -> {
                    try {
                        sel.setIsbn(isbnF.getText().trim());
                        sel.setTitle(titleF.getText().trim());
                        sel.setAuthor(authF.getText().trim());
                        sel.setCategory(catF.getText().trim());
                        sel.setTotalCopies(Integer.parseInt(copF.getText().trim()));
                        bookDAO.updateBook(sel);
                        setStatus(status, "✅  Book updated successfully!", true);
                        isbnF.clear(); titleF.clear(); authF.clear(); catF.clear(); copF.clear();
                        addBtn.setText("➕  Add Book");
                        addBtn.setStyle("-fx-background-color:#15803D;-fx-text-fill:white;-fx-font-size:13px;-fx-font-weight:bold;-fx-background-radius:8;-fx-cursor:hand;-fx-padding:0 18px;");
                        refreshBooks(table);
                    } catch (Exception ex) {
                        setStatus(status, "⚠  Fill all fields correctly.", false);
                    }
                });
            } else setStatus(status, "⚠  Select a book first.", false);
        });

        searchBtn.setOnAction(e -> {
            if (!searchF.getText().trim().isEmpty())
                table.setItems(FXCollections.observableArrayList(
                        bookDAO.searchBooks(searchF.getText().trim(), filterBox.getValue())));
        });
        showAllBtn.setOnAction(e -> refreshBooks(table));
        deleteBtn.setOnAction(e -> {
            Book sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION,
                        "Delete '" + sel.getTitle() + "'?", ButtonType.YES, ButtonType.NO);
                a.showAndWait().ifPresent(r -> {
                    if (r == ButtonType.YES) { bookDAO.deleteBook(sel.getBookId());
                        setStatus(status, "✅  Book deleted.", true); refreshBooks(table); }
                });
            } else setStatus(status, "⚠  Select a book first.", false);
        });

        VBox layout = new VBox(16, formCard, searchBar, table);
        layout.setPadding(new Insets(24));
        ScrollPane sp = new ScrollPane(layout);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:#F1F5F9;-fx-background:#F1F5F9;");
        return sp;
    }

    private ScrollPane buildMembersPage() {
        Label formTitle = sectionHeader("➕  Register New Member");
        TextField nameF  = inputField("Full Name");
        TextField emailF = inputField("Email Address");
        TextField phoneF = inputField("Phone Number");
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Student", "Faculty", "Public");
        typeBox.setValue("Student");
        typeBox.setStyle(comboStyle());
        typeBox.setPrefHeight(40);
        typeBox.setPrefWidth(200);

        Button addBtn = actionButton("➕  Register", "#15803D");
        Label status  = new Label("");
        status.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));

        GridPane form = new GridPane();
        form.setHgap(14); form.setVgap(14);
        form.add(fLbl("Name:"),  0, 0); form.add(nameF,   1, 0);
        form.add(fLbl("Email:"), 2, 0); form.add(emailF,  3, 0);
        form.add(fLbl("Phone:"), 0, 1); form.add(phoneF,  1, 1);
        form.add(fLbl("Type:"),  2, 1); form.add(typeBox, 3, 1);
        form.add(addBtn,         3, 2);
        VBox formCard = pageCard(formTitle, form, status);

        TextField searchF = inputField("🔍  Search members...");
        searchF.setPrefWidth(280);
        Button searchBtn  = actionButton("Search",      "#1D4ED8");
        Button showAllBtn = actionButton("Show All",    "#7C3AED");
        Button historyBtn = actionButton("📋 History",  "#B45309");
        Button editBtn    = actionButton("✏  Edit",     "#0D9488");
        Button deleteBtn  = actionButton("🗑 Delete",   "#DC2626");

        HBox searchBar = new HBox(10, searchF, searchBtn, showAllBtn, historyBtn, editBtn, deleteBtn);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        TableView<Member> table = new TableView<>();
        table.setStyle("-fx-background-color:white;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<Member,Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("memberId")); idCol.setPrefWidth(55);
        TableColumn<Member,String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Member,String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<Member,String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        TableColumn<Member,String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("memberType")); typeCol.setPrefWidth(90);
        TableColumn<Member,String> dateCol = new TableColumn<>("Join Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("joinDate")); dateCol.setPrefWidth(110);
        table.getColumns().addAll(idCol, nameCol, emailCol, phoneCol, typeCol, dateCol);
        refreshMembers(table);

        addBtn.setOnAction(e -> {
            try {
                memberDAO.addMember(new Member(0, nameF.getText().trim(),
                        emailF.getText().trim(), phoneF.getText().trim(),
                        typeBox.getValue(), LocalDate.now()));
                setStatus(status, "✅  Member registered!", true);
                nameF.clear(); emailF.clear(); phoneF.clear();
                addBtn.setText("➕  Register");
                addBtn.setStyle("-fx-background-color:#15803D;-fx-text-fill:white;-fx-font-size:13px;-fx-font-weight:bold;-fx-background-radius:8;-fx-cursor:hand;-fx-padding:0 18px;");
                refreshMembers(table);
            } catch (Exception ex) {
                setStatus(status, "⚠  Fill all fields.", false);
            }
        });

        editBtn.setOnAction(e -> {
            Member sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                nameF.setText(sel.getName());
                emailF.setText(sel.getEmail());
                phoneF.setText(sel.getPhone());
                typeBox.setValue(sel.getMemberType());
                setStatus(status, "✏  Editing: " + sel.getName() + " — modify fields and click Update.", true);
                addBtn.setText("💾  Update Member");
                addBtn.setStyle("-fx-background-color:#0D9488;-fx-text-fill:white;-fx-font-size:13px;-fx-font-weight:bold;-fx-background-radius:8;-fx-cursor:hand;-fx-padding:0 18px;");
                addBtn.setOnAction(ev -> {
                    try {
                        sel.setName(nameF.getText().trim());
                        sel.setEmail(emailF.getText().trim());
                        sel.setPhone(phoneF.getText().trim());
                        sel.setMemberType(typeBox.getValue());
                        memberDAO.updateMember(sel);
                        setStatus(status, "✅  Member updated successfully!", true);
                        nameF.clear(); emailF.clear(); phoneF.clear();
                        addBtn.setText("➕  Register");
                        addBtn.setStyle("-fx-background-color:#15803D;-fx-text-fill:white;-fx-font-size:13px;-fx-font-weight:bold;-fx-background-radius:8;-fx-cursor:hand;-fx-padding:0 18px;");
                        refreshMembers(table);
                    } catch (Exception ex) {
                        setStatus(status, "⚠  Fill all fields correctly.", false);
                    }
                });
            } else setStatus(status, "⚠  Select a member first.", false);
        });

        searchBtn.setOnAction(e -> {
            if (!searchF.getText().trim().isEmpty())
                table.setItems(FXCollections.observableArrayList(memberDAO.searchMembers(searchF.getText().trim())));
        });
        showAllBtn.setOnAction(e -> refreshMembers(table));
        historyBtn.setOnAction(e -> {
            Member sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) showMemberHistory(sel);
            else setStatus(status, "⚠  Select a member first.", false);
        });
        deleteBtn.setOnAction(e -> {
            Member sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION,
                        "Delete '" + sel.getName() + "'?", ButtonType.YES, ButtonType.NO);
                a.showAndWait().ifPresent(r -> {
                    if (r == ButtonType.YES) { memberDAO.deleteMember(sel.getMemberId());
                        setStatus(status, "✅  Member deleted.", true); refreshMembers(table); }
                });
            } else setStatus(status, "⚠  Select a member first.", false);
        });

        VBox layout = new VBox(16, formCard, searchBar, table);
        layout.setPadding(new Insets(24));
        ScrollPane sp = new ScrollPane(layout);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:#F1F5F9;-fx-background:#F1F5F9;");
        return sp;
    }

    private ScrollPane buildIssuePage() {
        Label formTitle = sectionHeader("📤  Issue Book to Member");
        TextField bookIdF   = inputField("Book ID");
        TextField memberIdF = inputField("Member ID");
        Button issueBtn     = actionButton("📤  Issue Book", "#1D4ED8");
        Label status        = new Label("");
        status.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        Label bookInfo   = infoLabel();
        Label memberInfo = infoLabel();

        bookIdF.setOnKeyReleased(e -> {
            try {
                Book b = bookDAO.getBookById(Integer.parseInt(bookIdF.getText().trim()));
                bookInfo.setText(b != null ? "📖  " + b.getTitle() + "  —  " + b.getAuthor() + "  |  Available: " + b.getAvailableCopies() : "❌  Book not found.");
            } catch (Exception ignored) { bookInfo.setText(""); }
        });
        memberIdF.setOnKeyReleased(e -> {
            try {
                Member m = memberDAO.getMemberById(Integer.parseInt(memberIdF.getText().trim()));
                memberInfo.setText(m != null ? "👤  " + m.getName() + "  |  " + m.getMemberType() + "  |  Loan: " + m.getLoanDays() + " days  |  Fine: £" + m.getFinePerDay() + "/day" : "❌  Member not found.");
            } catch (Exception ignored) { memberInfo.setText(""); }
        });

        GridPane form = new GridPane();
        form.setHgap(14); form.setVgap(10);
        form.add(fLbl("Book ID:"),   0, 0); form.add(bookIdF,   1, 0);
        form.add(bookInfo,           1, 1);
        form.add(fLbl("Member ID:"), 0, 2); form.add(memberIdF, 1, 2);
        form.add(memberInfo,         1, 3);
        form.add(issueBtn,           1, 4);
        VBox formCard = pageCard(formTitle, form, status);

        issueBtn.setOnAction(e -> {
            try {
                int bookId = Integer.parseInt(bookIdF.getText().trim());
                int memId  = Integer.parseInt(memberIdF.getText().trim());
                boolean ok = transactionDAO.issueBook(bookId, memId);
                if (ok) {
                    Member m = memberDAO.getMemberById(memId);
                    String due = LocalDate.now().plusDays(m != null ? m.getLoanDays() : 14).toString();
                    setStatus(status, "✅  Book issued! Due: " + due, true);
                    bookIdF.clear(); memberIdF.clear();
                    bookInfo.setText(""); memberInfo.setText("");
                } else setStatus(status, "⚠  Book unavailable or member not found.", false);
            } catch (Exception ex) { setStatus(status, "⚠  Enter valid Book ID and Member ID.", false); }
        });

        Label activeTitle = sectionHeader("📋  Currently Borrowed Books");
        TableView<Transaction> table = new TableView<>();
        table.setStyle("-fx-background-color:white;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(300);
        TableColumn<Transaction,Integer> txIdCol = new TableColumn<>("TX ID");
        txIdCol.setCellValueFactory(new PropertyValueFactory<>("transactionId")); txIdCol.setPrefWidth(70);
        TableColumn<Transaction,String> bookCol = new TableColumn<>("Book Title");
        bookCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        TableColumn<Transaction,String> memCol = new TableColumn<>("Member");
        memCol.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        TableColumn<Transaction,String> borCol = new TableColumn<>("Borrow Date");
        borCol.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        TableColumn<Transaction,String> dueCol = new TableColumn<>("Due Date");
        dueCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        table.getColumns().addAll(txIdCol, bookCol, memCol, borCol, dueCol, statusCol());
        table.setItems(FXCollections.observableArrayList(transactionDAO.getActiveTransactions()));

        VBox layout = new VBox(16, formCard, activeTitle, table);
        layout.setPadding(new Insets(24));
        ScrollPane sp = new ScrollPane(layout);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:#F1F5F9;-fx-background:#F1F5F9;");
        return sp;
    }

    private ScrollPane buildReturnPage() {
        Label formTitle = sectionHeader("📥  Process Book Return");
        TextField txIdF  = inputField("Transaction ID");
        Button returnBtn = actionButton("📥  Process Return", "#B45309");
        Label status     = new Label("");
        status.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        Label txInfo = infoLabel();

        GridPane form = new GridPane();
        form.setHgap(14); form.setVgap(10);
        form.add(fLbl("TX ID:"), 0, 0); form.add(txIdF,    1, 0);
        form.add(txInfo,         1, 1);
        form.add(returnBtn,      1, 2);
        VBox formCard = pageCard(formTitle, form, status);

        returnBtn.setOnAction(e -> {
            try {
                int txId    = Integer.parseInt(txIdF.getText().trim());
                double fine = transactionDAO.returnBook(txId);
                if (fine == -1) setStatus(status, "⚠  Not found or already returned.", false);
                else if (fine > 0) setStatus(status, "✅  Returned. Fine: £" + fine + " — Collect from member!", false);
                else setStatus(status, "✅  Returned. No fine charged.", true);
                txIdF.clear(); txInfo.setText("");
            } catch (Exception ex) { setStatus(status, "⚠  Enter valid Transaction ID.", false); }
        });

        Label activeTitle = sectionHeader("📋  Click a Row to Select Transaction");
        TableView<Transaction> table = new TableView<>();
        table.setStyle("-fx-background-color:white;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(380);
        TableColumn<Transaction,Integer> txIdCol = new TableColumn<>("TX ID");
        txIdCol.setCellValueFactory(new PropertyValueFactory<>("transactionId")); txIdCol.setPrefWidth(70);
        TableColumn<Transaction,String> bookCol = new TableColumn<>("Book Title");
        bookCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        TableColumn<Transaction,String> memCol = new TableColumn<>("Member");
        memCol.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        TableColumn<Transaction,String> borCol = new TableColumn<>("Borrow Date");
        borCol.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        TableColumn<Transaction,String> dueCol = new TableColumn<>("Due Date");
        dueCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        table.getColumns().addAll(txIdCol, bookCol, memCol, borCol, dueCol, statusCol());
        table.setItems(FXCollections.observableArrayList(transactionDAO.getActiveTransactions()));
        table.setOnMouseClicked(e -> {
            Transaction sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                txIdF.setText(String.valueOf(sel.getTransactionId()));
                txInfo.setText("📖  " + sel.getBookTitle() + "  |  👤  " + sel.getMemberName() + "  |  Due: " + sel.getDueDate());
            }
        });

        VBox layout = new VBox(16, formCard, activeTitle, table);
        layout.setPadding(new Insets(24));
        ScrollPane sp = new ScrollPane(layout);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:#F1F5F9;-fx-background:#F1F5F9;");
        return sp;
    }

    private ScrollPane buildFinesPage() {
        Label title       = sectionHeader("💰  Outstanding Fines");
        Button refreshBtn = actionButton("🔄  Refresh",      "#1D4ED8");
        Button paidBtn    = actionButton("✅  Mark as Paid", "#15803D");
        Label status      = new Label("");
        status.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        HBox toolbar = new HBox(10, refreshBtn, paidBtn, status);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        TableView<Fine> table = new TableView<>();
        table.setStyle("-fx-background-color:white;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);
        TableColumn<Fine,Integer> fineIdCol = new TableColumn<>("Fine ID");
        fineIdCol.setCellValueFactory(new PropertyValueFactory<>("fineId")); fineIdCol.setPrefWidth(75);
        TableColumn<Fine,Integer> txCol2 = new TableColumn<>("TX ID");
        txCol2.setCellValueFactory(new PropertyValueFactory<>("transactionId")); txCol2.setPrefWidth(75);
        TableColumn<Fine,String> memCol = new TableColumn<>("Member");
        memCol.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        TableColumn<Fine,String> bookCol = new TableColumn<>("Book");
        bookCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        TableColumn<Fine,Double> amtCol = new TableColumn<>("Amount (£)");
        amtCol.setCellValueFactory(new PropertyValueFactory<>("amount")); amtCol.setPrefWidth(110);
        TableColumn<Fine,String> paidCol = new TableColumn<>("Paid");
        paidCol.setCellValueFactory(new PropertyValueFactory<>("paid")); paidCol.setPrefWidth(80);
        table.getColumns().addAll(fineIdCol, txCol2, memCol, bookCol, amtCol, paidCol);
        table.setItems(FXCollections.observableArrayList(transactionDAO.getUnpaidFines()));

        refreshBtn.setOnAction(e -> table.setItems(FXCollections.observableArrayList(transactionDAO.getUnpaidFines())));
        paidBtn.setOnAction(e -> {
            Fine sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                transactionDAO.markFinePaid(sel.getFineId());
                setStatus(status, "✅  Fine marked as paid!", true);
                table.setItems(FXCollections.observableArrayList(transactionDAO.getUnpaidFines()));
            } else setStatus(status, "⚠  Select a fine first.", false);
        });

        VBox layout = new VBox(16, title, toolbar, table);
        layout.setPadding(new Insets(24));
        ScrollPane sp = new ScrollPane(layout);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:#F1F5F9;-fx-background:#F1F5F9;");
        return sp;
    }

    private ScrollPane buildReportsPage() {
        Label title = sectionHeader("📋  Library Reports");

        Label overdueTitle = new Label("⚠  Overdue Books");
        overdueTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        overdueTitle.setTextFill(Color.web("#B91C1C"));
        TableView<Transaction> overdueTable = new TableView<>();
        overdueTable.setStyle("-fx-background-color:white;");
        overdueTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        overdueTable.setPrefHeight(200);
        TableColumn<Transaction,Integer> ovTxCol = new TableColumn<>("TX ID");
        ovTxCol.setCellValueFactory(new PropertyValueFactory<>("transactionId")); ovTxCol.setPrefWidth(70);
        TableColumn<Transaction,String> ovBookCol = new TableColumn<>("Book Title");
        ovBookCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        TableColumn<Transaction,String> ovMemCol = new TableColumn<>("Member");
        ovMemCol.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        TableColumn<Transaction,String> ovDueCol = new TableColumn<>("Due Date");
        ovDueCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        overdueTable.getColumns().addAll(ovTxCol, ovBookCol, ovMemCol, ovDueCol, daysOverdueCol());
        overdueTable.setItems(FXCollections.observableArrayList(transactionDAO.getOverdueTransactions()));

        Label invTitle = new Label("📚  Book Inventory");
        invTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        invTitle.setTextFill(Color.web("#0D1B6E"));
        TableView<Book> invTable = new TableView<>();
        invTable.setStyle("-fx-background-color:white;");
        invTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        invTable.setPrefHeight(220);
        TableColumn<Book,Integer> invIdCol = new TableColumn<>("ID");
        invIdCol.setCellValueFactory(new PropertyValueFactory<>("bookId")); invIdCol.setPrefWidth(55);
        TableColumn<Book,String> invTitleCol = new TableColumn<>("Title");
        invTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Book,String> invAuthCol = new TableColumn<>("Author");
        invAuthCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        TableColumn<Book,String> invCatCol = new TableColumn<>("Category");
        invCatCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<Book,Integer> invTotCol = new TableColumn<>("Total");
        invTotCol.setCellValueFactory(new PropertyValueFactory<>("totalCopies")); invTotCol.setPrefWidth(65);
        TableColumn<Book,Integer> invAvCol = new TableColumn<>("Available");
        invAvCol.setCellValueFactory(new PropertyValueFactory<>("availableCopies")); invAvCol.setPrefWidth(85);
        invTable.getColumns().addAll(invIdCol, invTitleCol, invAuthCol, invCatCol, invTotCol, invAvCol);
        invTable.setItems(FXCollections.observableArrayList(bookDAO.getAllBooks()));

        Label memTitle = new Label("👥  All Members");
        memTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        memTitle.setTextFill(Color.web("#0D1B6E"));
        TableView<Member> memTable = new TableView<>();
        memTable.setStyle("-fx-background-color:white;");
        memTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        memTable.setPrefHeight(200);
        TableColumn<Member,Integer> mIdCol = new TableColumn<>("ID");
        mIdCol.setCellValueFactory(new PropertyValueFactory<>("memberId")); mIdCol.setPrefWidth(55);
        TableColumn<Member,String> mNmCol = new TableColumn<>("Name");
        mNmCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Member,String> mEmCol = new TableColumn<>("Email");
        mEmCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<Member,String> mTyCol = new TableColumn<>("Type");
        mTyCol.setCellValueFactory(new PropertyValueFactory<>("memberType")); mTyCol.setPrefWidth(90);
        TableColumn<Member,String> mDtCol = new TableColumn<>("Join Date");
        mDtCol.setCellValueFactory(new PropertyValueFactory<>("joinDate")); mDtCol.setPrefWidth(110);
        memTable.getColumns().addAll(mIdCol, mNmCol, mEmCol, mTyCol, mDtCol);
        memTable.setItems(FXCollections.observableArrayList(memberDAO.getAllMembers()));

        VBox layout = new VBox(14, title, overdueTitle, overdueTable, invTitle, invTable, memTitle, memTable);
        layout.setPadding(new Insets(24));
        ScrollPane sp = new ScrollPane(layout);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:#F1F5F9;-fx-background:#F1F5F9;");
        return sp;
    }

    private void showMemberHistory(Member member) {
        Stage popup = new Stage();
        popup.setTitle("History — " + member.getName());
        Label title = new Label("📋  Borrowing History:  " + member.getName() + "  (" + member.getMemberType() + ")");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        title.setTextFill(Color.web("#0D1B6E"));
        TableView<Transaction> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);
        TableColumn<Transaction,Integer> txIdCol = new TableColumn<>("TX ID");
        txIdCol.setCellValueFactory(new PropertyValueFactory<>("transactionId")); txIdCol.setPrefWidth(70);
        TableColumn<Transaction,String> bookCol = new TableColumn<>("Book Title");
        bookCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        TableColumn<Transaction,String> borCol = new TableColumn<>("Borrow Date");
        borCol.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        TableColumn<Transaction,String> dueCol = new TableColumn<>("Due Date");
        dueCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        TableColumn<Transaction,String> retCol = new TableColumn<>("Return Date");
        retCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        TableColumn<Transaction,String> stCol = new TableColumn<>("Status");
        stCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        table.getColumns().addAll(txIdCol, bookCol, borCol, dueCol, retCol, stCol);
        table.setItems(FXCollections.observableArrayList(transactionDAO.getMemberHistory(member.getMemberId())));
        VBox layout = new VBox(14, title, table);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color:#F1F5F9;");
        VBox.setVgrow(table, Priority.ALWAYS);
        popup.setScene(new Scene(layout, 820, 460));
        popup.show();
    }

    private void refreshBooks(TableView<Book> t) { t.setItems(FXCollections.observableArrayList(bookDAO.getAllBooks())); }
    private void refreshMembers(TableView<Member> t) { t.setItems(FXCollections.observableArrayList(memberDAO.getAllMembers())); }
    private void setStatus(Label l, String msg, boolean ok) { l.setText(msg); l.setTextFill(ok ? Color.web("#15803D") : Color.web("#DC2626")); }

    private void setActive(Button btn) {
        if (activeSidebarBtn != null) activeSidebarBtn.setStyle(sidebarStyle(false));
        btn.setStyle(sidebarStyle(true));
        activeSidebarBtn = btn;
    }

    private Button sidebarBtn(String icon, String label) {
        Button btn = new Button(icon + "   " + label);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(44);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        btn.setStyle(sidebarStyle(false));
        btn.setOnMouseEntered(e -> { if (btn != activeSidebarBtn) btn.setStyle("-fx-background-color:#1E3A8A;-fx-text-fill:white;-fx-background-radius:8;-fx-cursor:hand;-fx-padding:0 14px;"); });
        btn.setOnMouseExited(e -> { if (btn != activeSidebarBtn) btn.setStyle(sidebarStyle(false)); });
        return btn;
    }

    private String sidebarStyle(boolean active) {
        return "-fx-background-color:" + (active ? "#1D4ED8" : "transparent") + ";" +
                "-fx-text-fill:" + (active ? "white" : "#CBD5E1") + ";" +
                "-fx-background-radius:8;-fx-cursor:hand;-fx-padding:0 14px;";
    }

    private Label navSection(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
        l.setTextFill(Color.web("#475569"));
        l.setPadding(new Insets(14, 0, 4, 14));
        return l;
    }

    private VBox statCard(String icon, String title, String value, String color, String bg) {
        Label ic = new Label(icon); ic.setFont(Font.font("Segoe UI Emoji", 28));
        Label vl = new Label(value); vl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 36)); vl.setTextFill(Color.web(color));
        Label tl = new Label(title); tl.setFont(Font.font("Segoe UI", 12)); tl.setTextFill(Color.web("#6B7280"));
        VBox card = new VBox(4, ic, vl, tl);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20, 28, 20, 20));
        card.setStyle("-fx-background-color:" + bg + ";-fx-border-color:" + color + ";-fx-border-width:0 0 0 4;-fx-border-radius:12;-fx-background-radius:12;-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.07),8,0,0,2);");
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    private HBox policyCard(String icon, String type, String loan, String fine, String color) {
        Label ic = new Label(icon); ic.setFont(Font.font(18));
        Label tp = new Label(type); tp.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13)); tp.setTextFill(Color.web(color));
        Label ln = new Label(loan); ln.setFont(Font.font("Segoe UI", 12)); ln.setTextFill(Color.web("#374151"));
        Label fn = new Label(fine); fn.setFont(Font.font("Segoe UI", 12)); fn.setTextFill(Color.web("#6B7280"));
        VBox info = new VBox(3, tp, ln, fn);
        HBox card = new HBox(12, ic, info);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(14, 20, 14, 16));
        card.setStyle("-fx-background-color:white;-fx-border-color:#E5E7EB;-fx-border-radius:10;-fx-background-radius:10;-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.04),6,0,0,1);");
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    private VBox pageCard(Label title, GridPane form, Label status) {
        VBox c = new VBox(14, title, form, status);
        c.setPadding(new Insets(22));
        c.setStyle("-fx-background-color:white;-fx-border-color:#E5E7EB;-fx-border-radius:12;-fx-background-radius:12;-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.06),8,0,0,2);");
        return c;
    }

    private Label sectionHeader(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        l.setTextFill(Color.web("#0D1B6E"));
        return l;
    }

    private Label infoLabel() {
        Label l = new Label("");
        l.setFont(Font.font("Segoe UI", 12));
        l.setTextFill(Color.web("#374151"));
        return l;
    }

    private TextField inputField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefHeight(40);
        tf.setPrefWidth(220);
        tf.setStyle("-fx-background-color:white;-fx-border-color:#C7D2FE;-fx-border-radius:8;-fx-background-radius:8;-fx-font-size:13px;-fx-padding:0 10px;");
        return tf;
    }

    private TextField loginTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefHeight(48);
        tf.setMaxWidth(Double.MAX_VALUE);
        tf.setStyle(inputStyle());
        return tf;
    }

    private String inputStyle() {
        return "-fx-background-color:white;-fx-border-color:#C7D2FE;-fx-border-radius:10;-fx-background-radius:10;-fx-font-size:14px;-fx-padding:0 14px;";
    }

    private Button actionButton(String text, String color) {
        Button btn = new Button(text);
        btn.setPrefHeight(40);
        btn.setStyle("-fx-background-color:" + color + ";-fx-text-fill:white;-fx-font-size:13px;-fx-font-weight:bold;-fx-background-radius:8;-fx-cursor:hand;-fx-padding:0 18px;");
        btn.setOnMouseEntered(e -> btn.setOpacity(0.87));
        btn.setOnMouseExited(e  -> btn.setOpacity(1.0));
        return btn;
    }

    private Label fLbl(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        l.setTextFill(Color.web("#374151"));
        l.setMinWidth(85);
        return l;
    }

    private Label fieldLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        l.setTextFill(Color.web("#4B5563"));
        l.setPadding(new Insets(2, 0, 2, 0));
        return l;
    }

    private Label pill(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", 11));
        l.setTextFill(Color.web("#BFDBFE"));
        l.setStyle("-fx-background-color:#1E3A8A;-fx-background-radius:20;-fx-padding:4 12;");
        return l;
    }

    private String comboStyle() {
        return "-fx-background-color:white;-fx-border-color:#C7D2FE;-fx-border-radius:8;-fx-font-size:13px;";
    }

    private TableColumn<Transaction,String> statusCol() {
        TableColumn<Transaction,String> col = new TableColumn<>("Status");
        col.setCellValueFactory(new PropertyValueFactory<>("status"));
        col.setPrefWidth(110);
        col.setCellFactory(c -> new TableCell<>() {
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                Transaction tx = getTableView().getItems().get(getIndex());
                if (tx != null && tx.isOverdue()) { setText("⚠  OVERDUE"); setStyle("-fx-text-fill:#B91C1C;-fx-font-weight:bold;"); }
                else { setText("✅  " + item); setStyle("-fx-text-fill:#15803D;-fx-font-weight:bold;"); }
            }
        });
        return col;
    }

    private TableColumn<Transaction,String> daysOverdueCol() {
        TableColumn<Transaction,String> col = new TableColumn<>("Days Overdue");
        col.setCellValueFactory(data -> {
            long days = java.time.temporal.ChronoUnit.DAYS.between(data.getValue().getDueDate(), LocalDate.now());
            return new SimpleStringProperty(days + " days");
        });
        col.setPrefWidth(110);
        return col;
    }
}