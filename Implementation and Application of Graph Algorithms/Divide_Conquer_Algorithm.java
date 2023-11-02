import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Trading {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            int count = 0;
            String input = scanner.next();
            if (input == null) {
                System.exit(0);
            }
            int numPoints = Integer.valueOf(input);
            if (numPoints < 2 || numPoints > 100000) {
                System.exit(0);
            }
            Point[] pointList = new Point[numPoints];
            while (count < numPoints) {
                double x = scanner.nextDouble();
                double y = scanner.nextDouble();
                if (Math.abs(x) > 50000 || Math.abs(y) > 50000) {
                    System.exit(0);
                }
                pointList[count] = new Point(x, y);
                count++;
            }
            Arrays.sort(pointList, new XSort());
            double median = (pointList[0].x + pointList[pointList.length - 1].x) / 2;
            double leftDistance = getClosestLeft(pointList, median);
            double rightDistance = getClosestRight(pointList, median);

            double minDistance = Math.min(leftDistance, rightDistance);

            if (minDistance < 10000) {
                System.out.printf("%.4f %n", minDistance);
            } else {
                System.out.println("infinity");
            }
        }
    }

    public static double getClosestLeft(Point[] points, double median) {
        double minDistance = 100000;
        for (int i = 0; i < points.length; i++) {
            if (points[i].x <= median) {
                for (int j = i + 1; j < points.length; j++) {
                    if (points[j].x <= median) {
                        double distance = Math.sqrt(Math.pow(points[j].x - points[i].x, 2) + Math.pow(points[j].y - points[i].y, 2));
                        minDistance = Math.min(minDistance, distance);
                    }
                }
            }
        }
        return minDistance;
    }

    public static double getClosestRight(Point[] points, double median) {
        double minDistance = 100000;
        for (int i = 0; i < points.length; i++) {
            if (points[i].x > median) {
                for (int j = i + 1; j < points.length; j++) {
                    if (points[j].x > median) {
                        double distance = Math.sqrt(Math.pow(points[j].x - points[i].x, 2) + Math.pow(points[j].y - points[i].y, 2));
                        minDistance = Math.min(minDistance, distance);
                    }
                }
            }
        }
        return minDistance;
    }

    static class Point {
        double x;
        double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    static class XSort implements Comparator<Point> {
        public int compare(Point s1, Point s2) {
            return Double.compare(s1.x, s2.x);
        }
    }
}
