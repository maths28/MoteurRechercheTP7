package algorithms;

import java.awt.Point;
import java.util.ArrayList;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class DefaultTeam {

    public static final int DISTANCE_POINT = 55;

    public static int recNumber = 0;

    public ArrayList<Point> calculDominatingSet(ArrayList<Point> points, int edgeThreshold) {

        //Creation des listes
        ArrayList<Point> result = null ;

        /*
        //random
        //best of ten
        int max = 200 ;
        for (int random = 0; random < max; random++){
            System.out.println("Tour : " + random);
            //glouton
            ArrayList<Point> pointsLeft = (ArrayList<Point>) points.clone();
            List<Point> mostLinkedPointList = null;
            ArrayList<Point> currentResult = new ArrayList<Point>();
            while (pointsLeft.size() > 0) {
                //System.out.println("----------------" + pointsLeft.size());
                //trouver le point avec le plus gourmand dans ceux restants
                Point mostLinkedPoint = null;
                List<Point> linked = null;
                int mostLinkedPointNbr = -1;
                for (Point point : pointsLeft) {
                    linked = findAllLinkedPoints(point, pointsLeft);
                    int nbrlinks = linked.size();
                    if (
                            (nbrlinks > mostLinkedPointNbr)
                            ||
                            (nbrlinks == mostLinkedPointNbr && new Random().nextInt(max) < random)
                    ) {
                        mostLinkedPoint = point;
                        mostLinkedPointNbr = nbrlinks;
                        mostLinkedPointList = linked;
                    }
                }
                //retirer les points de la liste
                pointsLeft.remove(mostLinkedPoint);
                currentResult.add(mostLinkedPoint);
                for (Point point : mostLinkedPointList) {
                    pointsLeft.remove(point);
                }
            }

            //comparaison
            //System.out.println(currentResult) ;
            if (result == null || result.size() > currentResult.size()){
                result = currentResult ;
            }
        }
        //System.out.println(result) ;
        */
        result = recursiveDominatingSet(points) ;
        return result;
    }

    private ArrayList<Point> recursiveDominatingSet(ArrayList<Point> allPoints) {
        if(allPoints == null){
            return new ArrayList<Point>() ;
        }
        System.out.println("-debut--- recurence numero :" + recNumber) ;
        recNumber++ ;

        //
       /* if(recNumber < 100) {
            System.out.println("taille courante :" + allPoints.size());
        }*/



        ArrayList<Point> result = new ArrayList<Point>() ;
        HashMap<Point, ArrayList> possibleResult = new HashMap<Point, ArrayList>() ;

        List<Point> linked = null ;
        List<Point> mostLinkedPoints = new ArrayList<Point>() ;
        int mostLinkedPointNbr = -1 ;
        for (Point point : allPoints){
            linked = findAllLinkedPoints(point, allPoints);
            int nbrlinks = linked.size();
            if (nbrlinks > mostLinkedPointNbr){
                mostLinkedPoints = new ArrayList<Point>();
                mostLinkedPoints.add(point) ;
                mostLinkedPointNbr = nbrlinks;
            }
            else if(nbrlinks == mostLinkedPointNbr) {
                mostLinkedPoints.add(point) ;
            }
        }
        int stop = 0;
        int limit = 1 ;
        for (Point point : mostLinkedPoints){
            ArrayList<Point> list = (ArrayList<Point>)allPoints.clone() ;
            list.remove(point);
            for (Point point2 : findAllLinkedPoints(point, allPoints)){
                list.remove(point2);
            }
            possibleResult.put(point ,recursiveDominatingSet(list)) ;
            if (stop >= limit){
                break ;
            }
            stop++ ;
        }

        for(HashMap.Entry<Point, ArrayList> entry : possibleResult.entrySet()) {
            ArrayList currentResult = entry.getValue();
            Point point = entry.getKey() ;
            //System.out.println(currentResult);
            if (result.size() == 0 || result.size() > currentResult.size()) {
                result = currentResult;
                //System.out.println(result);
                result.add(point) ;
            }
        }
        //System.out.println("---fin--- recurence numero :" + recNumber) ;
        return result;
    }


        private List<Point> findAllLinkedPoints(Point point, List<Point> allPoints) {
        List<Point> result = new ArrayList<>();
        for (Point p : allPoints) {
            if (!p.equals(point) && p.distance(point) <= DISTANCE_POINT) {
                result.add(p);
            }
        }
        return result;

    }

    private int countLinks(Point point, List<Point> allPoints) {
        return findAllLinkedPoints(point, allPoints).size();
    }

    //FILE PRINTER
    private void saveToFile(String filename, ArrayList<Point> result) {
        int index = 0;
        try {
            while (true) {
                BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filename + Integer.toString(index) + ".points")));
                try {
                    input.close();
                } catch (IOException e) {
                    System.err.println("I/O exception: unable to close " + filename + Integer.toString(index) + ".points");
                }
                index++;
            }
        } catch (FileNotFoundException e) {
            printToFile(filename + Integer.toString(index) + ".points", result);
        }
    }

    private void printToFile(String filename, ArrayList<Point> points) {
        try {
            PrintStream output = new PrintStream(new FileOutputStream(filename));
            int x, y;
            for (Point p : points)
                output.println(Integer.toString((int) p.getX()) + " " + Integer.toString((int) p.getY()));
            output.close();
        } catch (FileNotFoundException e) {
            System.err.println("I/O exception: unable to create " + filename);
        }
    }

    //FILE LOADER
    private ArrayList<Point> readFromFile(String filename) {
        String line;
        String[] coordinates;
        ArrayList<Point> points = new ArrayList<Point>();
        try {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(new FileInputStream(filename))
            );
            try {
                while ((line = input.readLine()) != null) {
                    coordinates = line.split("\\s+");
                    points.add(new Point(Integer.parseInt(coordinates[0]),
                            Integer.parseInt(coordinates[1])));
                }
            } catch (IOException e) {
                System.err.println("Exception: interrupted I/O.");
            } finally {
                try {
                    input.close();
                } catch (IOException e) {
                    System.err.println("I/O exception: unable to close " + filename);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found.");
        }
        return points;
    }
}
