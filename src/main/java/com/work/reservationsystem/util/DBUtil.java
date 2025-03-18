package com.work.reservationsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
   private static final String URL = "jdbc:mysql://localhost:3306/fitness_reservation_system?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai";
   private static final String USERNAME = "root"; 
   private static final String PASSWORD = "";

   static {
       try {
           Class.forName("com.mysql.cj.jdbc.Driver");
       } catch (ClassNotFoundException e) {
           throw new RuntimeException("データベースドライバーの読み込みに失敗しました", e);
       }
   }

   public static Connection getConnection() throws SQLException {
       return DriverManager.getConnection(URL, USERNAME, PASSWORD);
   }
}