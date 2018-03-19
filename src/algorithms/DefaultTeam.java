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
import java.util.List;

public class DefaultTeam {

    public static final int DISTANCE_POINT = 55;

    public ArrayList<Point> calculDominatingSet(ArrayList<Point> points, int edgeThreshold) {

        //Creation des listes
        ArrayList<Point> result = new ArrayList<Point>();
        ArrayList<Point> pointsLeft = (ArrayList<Point>) points.clone();
        List<Point> mostLinkedPointList = null ;
        while(pointsLeft.size() > 0) {
            System.out.println("----------------"+pointsLeft.size()) ;
            //trouver le point avec le plus gourmand dans ceux restants
            Point mostLinkedPoint = null;
            List<Point> linked = null;
            int mostLinkedPointNbr = -1;
            for (Point point : pointsLeft) {
                linked = findAllLinkedPoints(point, pointsLeft);
                int nbrlinks = linked.size();
                if (nbrlinks > mostLinkedPointNbr) {
                    mostLinkedPoint = point;
                    mostLinkedPointNbr = nbrlinks;
                    mostLinkedPointList = linked ;
                }
            }
            //retirer les points de la liste
            pointsLeft.remove(mostLinkedPoint);
            result.add(mostLinkedPoint);
            for (Point point : mostLinkedPointList) {
                pointsLeft.remove(point);
            }
        }
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
