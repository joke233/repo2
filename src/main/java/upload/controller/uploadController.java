package upload.controller;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/file")
public class uploadController {

    // 文件上传
    @RequestMapping("/upload")
    public String upload(MultipartFile upload, HttpServletRequest request) throws IOException {

        String fileName = "";
        // 定义文件名
        String uploadName = upload.getOriginalFilename();
        // 获取扩展名
        String exeName = uploadName.substring(uploadName.lastIndexOf(".") + 1);
        // 获取uuid值
        String uuid = UUID.randomUUID().toString().replace("-", "");
        // 拼接不重复的文件名
        fileName = uuid + "_" + uploadName;
        // 定义文件夹名
        String realPath = request.getServletContext().getRealPath("/WEB-INF/uploads");
        String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        File dirFile = new File(realPath + "/" + time);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        // 开始上传
        File file = new File(dirFile, fileName);
        upload.transferTo(file);
        System.out.println(file.getAbsolutePath()); // todo

        return "success";
    }

    // 文件上传2
    @RequestMapping("/upload2")
    public String upload2(HttpServletRequest request) throws Exception {

        // 定义文件夹名
        String realPath = request.getServletContext().getRealPath("/WEB-INF/uploads");
        String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        File dirFile = new File(realPath + "/" + time);
        if (!dirFile.exists()){
            dirFile.mkdir();
        }

        // 获取上传服务程序
        ServletFileUpload fileUpload = new ServletFileUpload(new DiskFileItemFactory());

        // 获取所有的上传项目
        List<FileItem> fileItems = fileUpload.parseRequest(request);

        for (FileItem item : fileItems) {
            if (!item.isFormField()) {
                // 获取uuid值
                String name = UUID.randomUUID().toString().replace("-", "") + "_" + item.getName();
                // 开始上传
                item.write(new File(realPath, name));
                // 删除临时文件
                item.delete();
                System.out.println(realPath + "/" + name); // todo log
            }
        }
        return "success";
    }


}
