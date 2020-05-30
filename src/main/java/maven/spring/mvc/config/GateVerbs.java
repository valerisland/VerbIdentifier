package maven.spring.mvc.config;

import gate.*;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.persist.PersistenceException;
import gate.util.persistence.PersistenceManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GateVerbs {


    public static ArrayList FindVerbs(Document doc, String verbtime) {
        ArrayList<String> verbs = new ArrayList<>();
        AnnotationSet annotationSet = doc.getAnnotations("Verbs").get(verbtime);
        System.out.println(annotationSet);
        if (annotationSet.isEmpty())
        {
            verbs.add("--no verbs of this tense found--");
        }
        else {
            for (Annotation annotation : annotationSet) {
                String verb = Utils.stringFor(doc, annotation);
                verbs.add(verb);
                System.out.println(verb);
            }
        }
        return verbs;
    }
}
