package com.work.reservationsystem.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.alibaba.fastjson2.JSON;

public class ImageUploadServlet {
    
    public void upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("===== [DEBUG] Entering ImageUploadServlet.upload() =====");
        
        try {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            System.out.println("[DEBUG] isMultipart = " + isMultipart);
            if (!isMultipart) {
                System.out.println("[DEBUG] Request is not multipart, return error.");
                writeJson(response, "不支持的请求类型");
                return;
            }
            
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            
            // 获取运行时路径
            String webappPath = request.getServletContext().getRealPath("/");
            System.out.println("[DEBUG] Webapp path: " + webappPath);
            
            // 获取项目源代码路径
            String projectPath = getProjectPath(webappPath);
            System.out.println("[DEBUG] Project path: " + projectPath);
            
            // 确保运行时images目录存在
            File runtimeImagesDir = new File(webappPath, "images/coach");
            if (!runtimeImagesDir.exists()) {
                boolean created = runtimeImagesDir.mkdirs();
                System.out.println("[DEBUG] Created runtime images directory: " + created);
            }
            
            // 确保项目源码images目录存在
            File projectImagesDir = new File(projectPath, "src/main/webapp/images/coach");
            if (!projectImagesDir.exists()) {
                boolean created = projectImagesDir.mkdirs();
                System.out.println("[DEBUG] Created project images directory: " + created);
            }
            
            List<FileItem> items = upload.parseRequest(request);
            System.out.println("[DEBUG] Found " + items.size() + " FileItem(s).");
            
            boolean fileFound = false;
            
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    String fileName = item.getName();
                    System.out.println("[DEBUG] Original fileName = " + fileName);
                    
                    if (fileName != null && !fileName.isEmpty()) {
                        fileFound = true;
                        if (!fileName.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif)$")) {
                            System.out.println("[DEBUG] File type not allowed: " + fileName);
                            writeJson(response, "仅支持上传图片文件");
                            return;
                        }
                        
                        String ext = fileName.substring(fileName.lastIndexOf("."));
                        String newFileName = UUID.randomUUID().toString() + ext;
                        System.out.println("[DEBUG] newFileName = " + newFileName);
                        
                        // 保存到运行时目录
                        File runtimeFile = new File(runtimeImagesDir, newFileName);
                        System.out.println("[DEBUG] Saving to runtime: " + runtimeFile.getAbsolutePath());
                        item.write(runtimeFile);
                        
                        // 复制到项目源码目录
                        File projectFile = new File(projectImagesDir, newFileName);
                        System.out.println("[DEBUG] Copying to project: " + projectFile.getAbsolutePath());
                        Files.copy(runtimeFile.toPath(), projectFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        
                        Map<String, Object> result = new HashMap<>();
                        result.put("success", true);
                        result.put("message", "上传成功");
                        result.put("path", "/images/coach/" + newFileName);
                        writeJson(response, result);
                        return;
                    }
                }
            }
            
            if (!fileFound) {
                System.out.println("[DEBUG] No valid file found in items.");
                writeJson(response, "未找到上传文件");
            }
            
        } catch (Exception e) {
            System.out.println("[ERROR] Exception in upload:");
            e.printStackTrace();
            writeJson(response, "上传错误: " + e.getMessage());
        }
    }
    
    private String getProjectPath(String webappPath) {
        // 从运行时路径找到项目路径
        // wtpwebapps/项目名 -> 项目根目录
        try {
            // 首先获取到 .metadata 目录的父级，也就是 Workspace 目录
            File workspaceDir = new File(webappPath).getParentFile() // wtpwebapps
                    .getParentFile() // tmp1
                    .getParentFile() // eclipse.wst.server.core
                    .getParentFile() // .plugins
                    .getParentFile() // .metadata
                    .getParentFile(); // Workspace
            
            // 然后找到项目目录
            String projectName = webappPath.substring(webappPath.lastIndexOf("wtpwebapps/") + 11).split("/")[0];
            File projectDir = new File(workspaceDir, projectName);
            
            if (projectDir.exists()) {
                return projectDir.getAbsolutePath();
            }
            System.out.println("[DEBUG] Cannot find project directory: " + projectDir.getAbsolutePath());
            return null;
        } catch (Exception e) {
            System.out.println("[ERROR] Error finding project path: " + e.getMessage());
            return null;
        }
    }
    
    private void writeJson(HttpServletResponse response, String message) throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", message);
        writeJson(response, result);
    }
    
    private void writeJson(HttpServletResponse response, Map<String, Object> result) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(result));
    }
}