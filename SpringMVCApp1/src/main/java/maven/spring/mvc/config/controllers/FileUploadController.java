package maven.spring.mvc.config.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.stream.Collectors;

import gate.Corpus;
import gate.CorpusController;
import gate.Document;
import gate.Factory;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.persist.PersistenceException;
import gate.util.persistence.PersistenceManager;
import maven.spring.mvc.config.GateVerbs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import maven.spring.mvc.config.storage.StorageFileNotFoundException;
import maven.spring.mvc.config.storage.StorageService;
import maven.spring.mvc.config.GateVerbs;

@Controller
public class FileUploadController {

    private final StorageService storageService;
    private String text;
    private ArrayList<String> PresentSimpleActiveVerbs;
    private ArrayList<String> PresentSimplePassiveVerbs;
    private ArrayList<String> PresentContinuousActiveVerbs;
    private ArrayList<String> PresentContinuousPassiveVerbs;
    private ArrayList<String> PresentPerfectActiveVerbs;
    private ArrayList<String> PresentPerfectPassiveVerbs;
    private ArrayList<String> PresentPerfectContinuousActiveVerbs;
    private ArrayList<String> PresentPerfectContinuousPassiveVerbs;
    private ArrayList<String> PastSimpleActiveVerbs;
    private ArrayList<String> PastSimplePassiveVerbs;
    private ArrayList<String> PastContinuousActiveVerbs;
    private ArrayList<String> PastContinuousPassiveVerbs;
    private ArrayList<String> PastPerfectActiveVerbs;
    private ArrayList<String> PastPerfectPassiveVerbs;
    private ArrayList<String> PastPerfectContinuousActiveVerbs;
    private ArrayList<String> PastPerfectContinuousPassiveVerbs;
    private ArrayList<String> FutureSimpleActiveVerbs;
    private ArrayList<String> FutureSimplePassiveVerbs;
    private ArrayList<String> FutureContinuousActiveVerbs;
    private ArrayList<String> FutureContinuousPassiveVerbs;
    private ArrayList<String> FuturePerfectActiveVerbs;
    private ArrayList<String> FuturePerfectPassiveVerbs;
    private ArrayList<String> FuturePerfectContinuousActiveVerbs;
    private ArrayList<String> FuturePerfectContinuousPassiveVerbs;


    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws IOException, ResourceInstantiationException, PersistenceException, ExecutionException {

        if (file.getName().toLowerCase().endsWith(".txt")) {
            storageService.store(file);
        }
        else
        {
            redirectAttributes.addFlashAttribute("message",
                    "This is not txt " + file.getOriginalFilename() + "!");
        }
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        text = new String(file.getBytes(), StandardCharsets.UTF_8);
        redirectAttributes.addFlashAttribute("content", text);
        CorpusController application = (CorpusController)
                PersistenceManager.loadObjectFromFile(new File("src/main/resources/gate/VIapp2.xgapp"));
        Corpus corpus = Factory.newCorpus("StudWorks");
        application.setCorpus(corpus);
        Document doc = Factory.newDocument(text);
        corpus.add(doc);
        application.execute();

        PresentSimpleActiveVerbs = GateVerbs.FindVerbs(doc, "PresentSimpleActive");
        redirectAttributes.addFlashAttribute("verblist1", PresentSimpleActiveVerbs);

        PresentSimplePassiveVerbs = GateVerbs.FindVerbs(doc, "PresentSimplePassive");
        redirectAttributes.addFlashAttribute("verblist2", PresentSimplePassiveVerbs);

        PresentContinuousActiveVerbs = GateVerbs.FindVerbs(doc, "PresentContinuousActive");
        redirectAttributes.addFlashAttribute("verblist3", PresentContinuousActiveVerbs);

        PresentContinuousPassiveVerbs = GateVerbs.FindVerbs(doc, "PresentContinuousPassive");
        redirectAttributes.addFlashAttribute("verblist4", PresentContinuousPassiveVerbs);

        PresentPerfectActiveVerbs = GateVerbs.FindVerbs(doc, "PresentPerfectActive");
        redirectAttributes.addFlashAttribute("verblist5", PresentPerfectActiveVerbs);

        PresentPerfectPassiveVerbs = GateVerbs.FindVerbs(doc, "PresentPerfectPassive");
        redirectAttributes.addFlashAttribute("verblist6", PresentPerfectPassiveVerbs);

        PresentPerfectContinuousActiveVerbs = GateVerbs.FindVerbs(doc, "PresentPerfectContinuousActive");
        redirectAttributes.addFlashAttribute("verblist7", PresentPerfectContinuousActiveVerbs);

        PresentPerfectContinuousPassiveVerbs = GateVerbs.FindVerbs(doc, "PresentPerfectContinuousPassive");
        redirectAttributes.addFlashAttribute("verblist8", PresentPerfectContinuousPassiveVerbs);

        PastSimpleActiveVerbs = GateVerbs.FindVerbs(doc, "PastSimpleActive");
        redirectAttributes.addFlashAttribute("verblist9", PastSimpleActiveVerbs);

        PastSimplePassiveVerbs = GateVerbs.FindVerbs(doc, "PastSimplePassive");
        redirectAttributes.addFlashAttribute("verblist10", PastSimplePassiveVerbs);

        PastPerfectActiveVerbs = GateVerbs.FindVerbs(doc, "PastPerfectActive");
        redirectAttributes.addFlashAttribute("verblist11", PastPerfectActiveVerbs);

        PastPerfectPassiveVerbs = GateVerbs.FindVerbs(doc, "PastPerfectPassive");
        redirectAttributes.addFlashAttribute("verblist12", PastPerfectPassiveVerbs);

        PastContinuousActiveVerbs = GateVerbs.FindVerbs(doc, "PastContinuousActive");
        redirectAttributes.addFlashAttribute("verblist13", PastContinuousActiveVerbs);

        PastContinuousPassiveVerbs = GateVerbs.FindVerbs(doc, "PastContinuousPassive");
        redirectAttributes.addFlashAttribute("verblist14", PastContinuousPassiveVerbs);

        PastPerfectContinuousActiveVerbs = GateVerbs.FindVerbs(doc, "PastPerfectContinuousActive");
        redirectAttributes.addFlashAttribute("verblist15", PastPerfectContinuousActiveVerbs);

        PastPerfectContinuousPassiveVerbs = GateVerbs.FindVerbs(doc, "PastPerfectContinuousPassive");
        redirectAttributes.addFlashAttribute("verblist16", PastPerfectContinuousPassiveVerbs);

        FutureSimpleActiveVerbs = GateVerbs.FindVerbs(doc, "FutureSimpleActive");
        redirectAttributes.addFlashAttribute("verblist17", FutureSimpleActiveVerbs);

        FutureSimplePassiveVerbs = GateVerbs.FindVerbs(doc, "FutureSimplePassive");
        redirectAttributes.addFlashAttribute("verblist18", FutureSimplePassiveVerbs);

        FutureContinuousActiveVerbs = GateVerbs.FindVerbs(doc, "FutureContinuousActive");
        redirectAttributes.addFlashAttribute("verblist19", FutureContinuousActiveVerbs);

        FutureContinuousPassiveVerbs = GateVerbs.FindVerbs(doc, "FutureContinuousPassive");
        redirectAttributes.addFlashAttribute("verblist20", FutureContinuousPassiveVerbs);

        FuturePerfectActiveVerbs = GateVerbs.FindVerbs(doc, "FuturePerfectActive");
        redirectAttributes.addFlashAttribute("verblist21", FuturePerfectActiveVerbs);

        FuturePerfectPassiveVerbs = GateVerbs.FindVerbs(doc, "FuturePerfectPassive");
        redirectAttributes.addFlashAttribute("verblist22", FuturePerfectPassiveVerbs);

        FuturePerfectContinuousActiveVerbs = GateVerbs.FindVerbs(doc, "FuturePerfectContinuousActive");
        redirectAttributes.addFlashAttribute("verblist23", FuturePerfectContinuousActiveVerbs);

        FuturePerfectContinuousPassiveVerbs = GateVerbs.FindVerbs(doc, "FuturePerfectContinuousPassive");
        redirectAttributes.addFlashAttribute("verblist24", FuturePerfectContinuousPassiveVerbs);
        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
