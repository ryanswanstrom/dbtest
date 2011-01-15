package models;

import com.google.code.morphia.annotations.Entity;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import play.modules.morphia.Model;

/**
 *
 */
@Entity
public class TestRun extends Model {

    private static final double NANOS_PER_SEC = 1000000000d;
    private static final DecimalFormat DEC_FORMAT = new DecimalFormat("#.##");

    public int inserts;
    /** time in nonoseconds to insert into MongoHQ */
    public long mongoTime;
    /** time in nonoseconds to insert into xeround */
    public long xeTime;
    /** the number of inserts per sec into MongoHQ */
    public double mongoAvg;
    /** the number of inserts per sec into xeround */
    public double xeAvg;
    public double mongoTimeQ;
    public double xeTimeQ;
    public Date createdAt;

    public TestRun(int inserts, long mongoTime, long xeTime, long mongoQ, long xeQ) {
        this.inserts = inserts;
        this.mongoTime = mongoTime;
        this.xeTime = xeTime;
        this.createdAt = Calendar.getInstance().getTime();
		 
        double avg = this.inserts / (this.mongoTime / TestRun.NANOS_PER_SEC);
        this.mongoAvg = Double.valueOf(DEC_FORMAT.format(avg));
        avg = this.inserts / (this.xeTime / TestRun.NANOS_PER_SEC);
        this.xeAvg = Double.valueOf(DEC_FORMAT.format(avg));

        this.mongoTimeQ = Double.valueOf(DEC_FORMAT.format((mongoQ / TestRun.NANOS_PER_SEC)));
        this.xeTimeQ = Double.valueOf(DEC_FORMAT.format((xeQ / TestRun.NANOS_PER_SEC)));


    }

    @Override
    public String toString() {
        return "Inserts: " + Integer.toString(this.inserts) + ", MongoHQ Avg: " + this.mongoAvg
                + ", Xeround Avg: " + this.xeAvg;
    }

}
