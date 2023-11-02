import java.util.*;

public class MovingBoxes {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int numTestCases = in.nextInt();
        for (int count = 1; count <= numTestCases; count++) {
            int boxCapacity = in.nextInt();
            int minBoxCapacity = in.nextInt();
            int numCompanies = in.nextInt();
            Company[] companies = new Company[numCompanies];

            for (int i = 0; i < numCompanies; i++) {
                companies[i] = new Company(in.next(), in.nextInt(), in.nextInt());
            }

            printMinimumCost(boxCapacity, minBoxCapacity, companies, count);
        }
    }

    public static void printMinimumCost(int boxCapacity, int minBoxCapacity, Company[] companies, int caseNumber) {
        for (Company company : companies) {
            findMinimumCost(boxCapacity, minBoxCapacity, company, 0);
        }

        Arrays.sort(companies, new CompanyComparator());

        System.out.println("Case " + caseNumber);
        for (Company company : companies) {
            System.out.println(company.label + " " + (int) company.solution);
        }
    }

    public static void findMinimumCost(double boxCapacity, int minBoxCapacity, Company company, double costSoFar) {
        if (boxCapacity == minBoxCapacity) {
            company.solution = costSoFar;
            return;
        }

        if (boxCapacity - Math.ceil(boxCapacity / 2) < minBoxCapacity) {
            costSoFar = costSoFar + (boxCapacity - minBoxCapacity) * company.first;
            findMinimumCost(minBoxCapacity, minBoxCapacity, company, costSoFar);
        } else {
            if (Math.ceil(boxCapacity / 2) * company.first < company.second) {
                costSoFar = costSoFar + (Math.ceil(boxCapacity / 2) * company.first);
                findMinimumCost(boxCapacity - Math.ceil(boxCapacity / 2), minBoxCapacity, company, costSoFar);
            } else {
                costSoFar = costSoFar + company.second;
                findMinimumCost(boxCapacity - Math.ceil(boxCapacity / 2), minBoxCapacity, company, costSoFar);
            }
        }
    }

    public static class Company {
        String label;
        int first;
        int second;
        double solution;

        public Company(String label, int first, int second) {
            this.label = label;
            this.first = first;
            this.second = second;
        }
    }

    public static class CompanyComparator implements Comparator<Company> {
        public int compare(Company company1, Company company2) {
            if (company1.solution > company2.solution) {
                return 1;
            } else if (company1.solution < company2.solution) {
                return -1;
            }
            return 0;
        }
    }
}
