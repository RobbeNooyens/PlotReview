package me.robnoo02.plotreview.score;

import org.bukkit.OfflinePlayer;

public class Rating {
	
	public static void realisticRating() {

        //int TOTAL_STOC = 0;
        //double AVERAGE_SCORE = 0;
        //int PLOTS = 0;

        //high bracket
        for (int i = 0; i < 50; i++) {

            //PLOTS = 6;

            //double STOC_PER_PLOT = TOTAL_STOC / PLOTS;
            //double RATING = (TOTAL_STOC * STOC_PER_PLOT) * (AVERAGE_SCORE / 1000);

        }


    }
    
    public static int getRating(OfflinePlayer player) {
    	double totalStoc = 0;
    	double stocPerPlot = 0;
    	double averageScore = 0;
    	double RATING = (totalStoc * stocPerPlot) * (averageScore / 1000);
    	return (int) RATING;
    }
    

     private static int getStoc() {
         //gets stoc from config;
    	 return 0;
     }

     private static int getAvgScore() {
         //gets avg score from config;
    	 return 0;
     }

     private static int getPlots() {
         //gets num of plots from config;
    	 return 0;
     }

     public static int calculateNewRating(OfflinePlayer player) {
         //int OLD_RATING = getRating(player);
         int TOTAL_STOC = getStoc();
         int PLOTS = getPlots();
         double AVERAGE_SCORE = getAvgScore();

         double STOC_PER_PLOT = TOTAL_STOC / PLOTS;
         double RATING = (TOTAL_STOC * STOC_PER_PLOT) * (AVERAGE_SCORE / 1000);

         return (int) RATING;
     }

     public static void updateConfig(int newRating) {
         //Config.update.Rating(calculateNewRating());
     }
	

}
