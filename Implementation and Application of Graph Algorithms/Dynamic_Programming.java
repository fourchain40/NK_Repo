import java.util.*;

public class Drainage {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int num = in.nextInt();

        for (int count = 0; count < num; count++) {
            String location = in.next();
            int numRows = in.nextInt();
            int numCols = in.nextInt();
            Path[][] grid = new Path[numRows][numCols];

            // Initialize the grid
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    grid[i][j] = new Path(in.nextInt(), 0, i, j);
                }
            }

            // Calculate and print the result
            System.out.println(location + ": " + findPathNumber(grid));
        }

        in.close();
    }

    public static int findPathNumber(Path[][] grid) {
        int max = 0;
        int numRows = grid.length;
        int numCols = grid[0].length;

        // Traverse the grid and find paths for each cell
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (grid[i][j].path == 0) {
                    findPath(i, j, grid);
                }
                if (grid[i][j].path > max) {
                    max = grid[i][j].path;
                }
            }
        }

        return max;
    }

    public static int findPath(int i, int j, Path[][] grid) {
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        int maxPath = 1;

        for (int k = 0; k < 4; k++) {
            int x = i + dx[k];
            int y = j + dy[k];

            if (x >= 0 && x < grid.length && y >= 0 && y < grid[0].length && grid[x][y].num < grid[i][j].num) {
                if (grid[x][y].path == 0) {
                    grid[x][y].path = findPath(x, y, grid);
                }
                maxPath = Math.max(maxPath, 1 + grid[x][y].path);
            }
        }

        grid[i][j].path = maxPath;
        return maxPath;
    }

    public static class Path {
        int num;
        int path;
        int r;
        int c;

        public Path(int num, int path, int r, int c) {
            this.num = num;
            this.path = path;
            this.r = r;
            this.c = c;
        }
    }
}
