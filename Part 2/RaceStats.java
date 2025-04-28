import java.io.Serializable;
import java.util.Date;

public class RaceStats implements Serializable{
    
    private final Date raceDate;
    private final int position;
    private final double speed;
    private final double time;
    private final boolean fell;
    private final double confidenceChange;

    public RaceStats(Date date, int position, double speed, double time, boolean fell, double confidenceChange){

        raceDate = date;
        this.position = position;
        this.speed = speed;
        this.time = time;
        this.fell = fell;
        this.confidenceChange = confidenceChange;
    }

    //Get total summary of stats for single race.
    public String getSummary() {

        return String.format("%tF: Pos %d | Speed %.2f m/s | Time %.1fs | %s | Conf Δ%.2f",
        raceDate, position, speed, time, fell ? "Fell" : "Clean", confidenceChange);
    }

    public int getPosition(){
        return position;
    }
}


