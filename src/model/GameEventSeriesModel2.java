package ca.ubc.cs304.model;

import java.sql.Date;

public class GameEventSeriesModel2 {

        private final Date startDate;
        private final Date endDate;
        private final int duration;


        public GameEventSeriesModel2(Date startDate, Date endDate, int duration) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.duration = duration;
        }

        public Date getStartDate() {
            return startDate;
        }

        public Date getEndDate(){
            return endDate;
        }

        public int getDuration(){
            return duration;
        }

}
