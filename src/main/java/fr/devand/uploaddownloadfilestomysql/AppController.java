package fr.devand.uploaddownloadfilestomysql;

import fr.devand.uploaddownloadfilestomysql.model.Document;
import fr.devand.uploaddownloadfilestomysql.repository.DocumentRepository;
import fr.devand.uploaddownloadfilestomysql.service.DocumentService;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AppController {
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentService documentService;

    private Logger logger = LoggerFactory.getLogger(AppController.class);


    @GetMapping("/")
    public String viewHomePage() {
        return "home";

    }


    @GetMapping("/document")
    public String viewDocumentPage(Model model) {
        List<Document> listDocs = documentService.findAll();
        model.addAttribute("listDocs", listDocs);
        return "document";

    }

    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam("document") MultipartFile multipartFile,
            RedirectAttributes redirectAttributes
    ) throws IOException, Exception {
        String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String pathWrite = "e:\\test\\upload";
        Document document = new Document();
        document.setName(filename);
//        document.setContent(multipartFile.getBytes());
        document.setSize(multipartFile.getSize());
        document.setPath(filename);
        document.setUploadTime(new Date());
        documentRepository.save(document);
        pathWrite = pathWrite + "\\" + filename;
        try {
            Files.write(Paths.get(pathWrite), filename.getBytes());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        String message = "upload ok";
        redirectAttributes.addFlashAttribute("message", message);
        if (document.getSize() > 10000000) {
            throw new IOException("file > 10MB");

        }
        return "redirect:/document";
    }

    @ExceptionHandler({IOException.class, java.sql.SQLException.class, Exception.class})
    public ModelAndView handleIOException(Exception ex) {
        ModelAndView model = new ModelAndView("error");

        model.addObject("exception", ex.getMessage());

        return model;
    }

    @ExceptionHandler(IOException.class)
    public ModelAndView handleIOException(IOException ex) {
        ModelAndView model = new ModelAndView("error");
        String exception = "size error";
        model.addObject("exception", exception);

        return model;
    }
    @GetMapping("/download")
    public void downloadFile(@Param("name") String name, HttpServletResponse response) throws Exception {
        documentRepository.findByName(name);
        Optional<Document> result = documentRepository.findByName(name);
        if (!result.isPresent()) {
            throw new Exception("No Document id :" + name);
        }

        Document document = result.get();
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename =" + document.getName();
        response.setHeader(headerKey, headerValue);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(document.getContent());
        outputStream.close();

    }

    @Controller
    @RequestMapping("/downloadFile")
    public class FileDownloadController
    {
        @RequestMapping("/files/{fileName:.+}")
        public void downloadPDFResource( HttpServletRequest request,
                                         HttpServletResponse response,
                                         @PathVariable("fileName") String fileName)
        {
            //If user is not authorized - he should be thrown out from here itself

            //Authorized user will download the file
            String dataDirectory = request.getServletContext().getRealPath("/WEB-INF/downloads/files/");
            Path file = Paths.get(dataDirectory, fileName);
            if (Files.exists(file))
            {

                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename="+fileName);
                try
                {
                    Files.copy(file, response.getOutputStream());
                    response.getOutputStream().flush();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

   /* @GetMapping("/download")
    public void downloadFile(@Param("id") Long id, HttpServletResponse response) throws Exception {
        documentRepository.findById(id);
        Optional<Document> result = documentRepository.findById(id);
        if (!result.isPresent()) {
            throw new Exception("No Document id :" + id);
        }

        Document document = result.get();
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename =" + document.getName();
        response.setHeader(headerKey, headerValue);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(document.getContent());
        outputStream.close();

    }*/
}
