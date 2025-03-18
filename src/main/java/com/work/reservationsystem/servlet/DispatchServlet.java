package com.work.reservationsystem.servlet;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/dispatch")
public class DispatchServlet extends HttpServlet {

    // クラスオブジェクトをキャッシュするマップ
    private static final Map<String, Class<?>> CLASS_MAP = new ConcurrentHashMap<>();

    // 実際に使用するオブジェクトをキャッシュするマップ
    private static final Map<String, Object> OBJECT_MAP = new ConcurrentHashMap<>();

    // ベースパッケージ
    private static final String BASE_PACKAGE = "com.work.reservationsystem.servlet.";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // エンコーディングの設定
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/html;charset=UTF-8");

        // リフレクションで呼び出すクラス名とメソッド名を取得
        String className = request.getParameter("className");
        String methodName = request.getParameter("methodName");
        if (className == null || "".equals(className) || methodName == null || "".equals(methodName)) {
            return;
        }

        // クラスオブジェクトを取得
        Class<?> cls = CLASS_MAP.get(className);
        if (cls == null) {
            try {
                cls = Class.forName(BASE_PACKAGE + className);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            CLASS_MAP.put(className, cls);
        }

        // 実際に使用するオブジェクトを取得
        Object obj = OBJECT_MAP.get(className);
        if (obj == null) {
            try {
                Constructor<?> constructor = cls.getConstructor();
                obj = constructor.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            OBJECT_MAP.put(className, obj);
        }

        try {
            // 呼び出すメソッドを取得
            Method method = cls.getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);

            // メソッドを実行
            method.invoke(obj, request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}