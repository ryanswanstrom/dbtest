package models;

import java.util.Calendar;
import java.util.Date;
import javax.persistence.Entity;
import play.data.validation.Min;
import play.db.jpa.Model;

/**
 *
 */
@Entity
public class LogMessageX extends Model {

    public String message;
    @Min(0)
    public Integer level;
    public String project;
    public Date createdAt;

    public LogMessageX(String mess, int lev, String proj) {
        this.message = mess;
        this.level = lev;
        this.project = proj;
        this.createdAt = Calendar.getInstance().getTime();
    }

    @Override
    public String toString() {
        return "xeround: " + this.message + ", " + this.level;
    }
}
