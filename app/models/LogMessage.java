package models;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import java.util.Calendar;
import java.util.Date;
import play.data.validation.Min;
import play.modules.morphia.Model;

/**
 *
 */
@Entity
public class LogMessage extends Model {

    @Indexed(value=IndexDirection.ASC, name="idx_message", dropDups=false)
    public String message;
    @Min(0)
    @Indexed(value=IndexDirection.ASC, name="idx_level", dropDups=false)
    public Integer level;
//    @Valid
//    @Embedded
    public String project;
    public Date createdAt;

    public LogMessage(String mess, int lev, String proj) {
        this.message = mess;
        this.level = lev;
        this.project = proj;//new Project(proj, val);
        this.createdAt = Calendar.getInstance().getTime();

    }

    @Override
    public String toString() {
        return "MongoHQ: " + this.message + ", " + this.level;
    }

}
