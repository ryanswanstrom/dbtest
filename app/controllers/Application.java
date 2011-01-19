package controllers;

import java.io.File;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import models.LogMessage;
import models.LogMessageX;
import models.TestRun;
import play.data.validation.Max;
import play.data.validation.MaxSize;
import play.data.validation.Min;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.db.jpa.JPA;
import play.modules.morphia.Model;
import play.modules.morphia.utils.MorphiaFixtures;
import play.mvc.Controller;
import play.test.Fixtures;

/**
 *
 */
public class Application extends Controller {

    public static void index() {
        long mongoNum = LogMessage.count();
        long xeNum = LogMessageX.count();
        // morphia query for mongodb
        List<LogMessage> mongoMess = LogMessage.filter("level <", 100).order("-createdAt").fetch(10);

        /*
         * With Xeround, I had to do a '=' here the '>' would not work after an
         * index had been created on level.
         */
        List<LogMessageX> xeMess = LogMessageX.find("byLevel", 900).fetch(10);

        List<TestRun> tests = TestRun.filter("inserts >", 1).order("-createdAt").fetch(10);
        
        render(mongoNum, xeNum, mongoMess, xeMess, tests);
    }

    public static void speedTest(@Required @Min(1) @Max(2000) int iters, @MaxSize(100) String message) {
        if (Validation.hasErrors()) {
            params.flash(); // add http parameters to the flash scope
            Validation.keep(); // keep the errors for the next request
            index();
        }

        long start = System.nanoTime();
        for (int i = 0; i < iters; i++) {
            LogMessage lm = new LogMessage(message, new Random().nextInt(1000), "Proj M");

            if (!lm.validateAndSave()) {
                params.flash(); // add http parameters to the flash scope
                Validation.keep(); // keep the errors for the next request
                index();
            }            
        }
        long end = System.nanoTime();
        long mongoTime = (end - start);

        start = System.nanoTime();
        EntityManager em = JPA.em();
        EntityTransaction tx = em.getTransaction();
        tx.commit();
        tx.begin();

        for (int i = 0; i < iters; i++) {
            LogMessageX lm = new LogMessageX(message, new Random().nextInt(1000), "Proj X");

            em.persist(lm);
            if ((i % 80) == 0) {
                tx.commit();
                tx.begin();
            }
//            if (!lm.validateAndSave()) {
//                params.flash(); // add http parameters to the flash scope
//                Validation.keep(); // keep the errors for the next request
//                index();
//            }

        }
        tx.commit();
        end = System.nanoTime();
        long xeTime = (end - start);

        /* now for query stuff */
        /* query with MongoHQ */
        start = System.nanoTime();
        for (int i =1; i < 1000; i++) {
            List<LogMessage> messages = LogMessage.filter("level =", i).fetchAll();
        }
        end = System.nanoTime();
        long mongoQueryTime = end - start;

        /* query with Xeround */
        start = System.nanoTime();
        for (int i =1; i < 1000; i++) {
            List<LogMessageX> messages = LogMessageX.find("level = ?", i).fetch();
        }
        end = System.nanoTime();
        long xeroundQueryTime = end - start;

        TestRun test = new TestRun(iters, mongoTime, xeTime, mongoQueryTime, xeroundQueryTime);
        
        test.save();
        speedResults(test.getId().toString());
    }

    public static void speedResults(String testRunId) {

        TestRun test = TestRun.findById(testRunId);
        render(test);
    }

    public static void deleteAll() {
        MorphiaFixtures.deleteAll();
        Fixtures.deleteAll();
        Model.ds().ensureIndexes();
        index();
    }

    public static void robots() {
        File file = play.Play.getFile("public/robots.txt");
        response.cacheFor("24h");
        renderBinary(file);
    }

    public static void about() {
        render();
    }

    public static void delete() {
        render();
    }
}
