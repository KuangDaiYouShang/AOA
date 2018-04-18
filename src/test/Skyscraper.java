import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

/*
 In task1, I use the algorithm introduced in Exam 1.
 In task2, I use an array to store the width of the candidate rectangles.
 In task3, I improved the data structure in task2 to reduce the time complexity.
 In task4, I use two i,j loops to get the len and wid of the rectangle. Then cpmpute the areas and
 eliminate yjpse areas which violate C.
 All the results have + 1 to start form 1 instead of 0.
  */

public class Skyscraper {
	private  int N;
	private  int M;
	private  int C;
	private  int[][] height = new int[N][M];

	Skyscraper(int n, int m , int c) {
		this.N =n;
		this.M = m;
		this.C = c;
		this.height = new int[N][M];
	}

	public  int[] task1() {
		//int[][] A = new int[N][M]; In order to save the space complexity,
		// we overwrite the height matrix.
		int max = 0;
		int[] result = new int[4];//create an array to store the coordinate of result.
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++ ) {
				if (height[i][j] != C) height[i][j] = 0;
				else if (i==0 || j ==0) {
					height[i][j] = 1;
				}
				else {
					height[i][j] = Math.min(height[i-1][j-1], Math.min(height[i-1][j], height[i][j-1])) + 1;
				}
				if(height[i][j] > max) {
					max = height[i][j];
					result[0] = i+1;
					result[1] = j+1;
					result[2] = i-height[i][j]+2;
					result[3] = j-height[i][j]+2;
				}
			}
		}
		return result;
	}

	public int[] task2() {
		int max = 0;
		int[] result = new int[4];
		int[] wid = new int[M];//create an array to record the width of the rectangle.
		for(int i = 0; i < M; i++) {
			wid[i] = 0;
		}
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				if (height[i][j] != C) wid[j] = 0;
				else wid[j] = wid[j] + 1;
			}
			for(int m = 0; m < M; m++) {
				int count = 0;
				int minhi = wid[m];
				//count counts the consecutive value from k, use two layer loops to find
				//out the length of the rectangle.
				if(wid[m] == 0) continue;
				for(int k = m; k < M && wid[k] != 0  && wid[m] != 0; k++) {
					if (wid[k] >= wid[m]) count++;
					else {
						minhi = wid[k];
						count++;
					}

				if(max < minhi*count) {
					max = minhi*count;
					result[0] = i+1;
					result[1] = m+count;
					result[2] = result[0] - minhi+1;
					result[3] = m+1;}
				}
			}
		}
		return result;
	}

	public int[] task3() {
		int max = 0;
		int[] result = new int[4];
		int[] wid = new int[M];//create an array to record the width of the rectangle.
		for(int i = 0; i < M; i++) {
			wid[i] = 0;
		}
		for(int i = 0; i < N; i++) {
			//Use a stack to replace two layer loop with one layer loop.
			//The stack is initialized for each i while wid[] is updated for each i.
			Stack<Integer> s = new Stack<>();
			for(int j = 0; j < M; j++) {
				if (height[i][j] != C) wid[j] = 0;
				else wid[j] = wid[j] + 1;
			}
			for (int k = 0; k <= M; k++) {
		        int h = (k == M ? 0 : wid[k]);
		        if(s.isEmpty() || h >= wid[s.peek()]){
		            s.push(k);
		        }else{
		            int tp = s.pop();
		            //max = Math.max(max, wid[tp] * (s.isEmpty() ? k : k - 1 - s.peek()));
		            if(max < wid[tp]*(s.isEmpty() ? k : k - 1 - s.peek())) {
		            	max = wid[tp]*(s.isEmpty() ? k : k - 1 - s.peek());
		            	result[0] = i+1;
		            	result[1] = k;
		            	result[2] = result[0] - wid[tp]+1;
		            	result[3] = k-1 - (s.isEmpty() ? k : k - 1 - s.peek()) + 2;
		            }
		            k--;
		        }
			}
		}
		return result;
	}

	public int[] task4() {
		int max =0;
		int dif[] = new int[C+1]; //create maximum area array with different c < C
		int[][] area = new int[N][M];
		int[][] len = new int[N][M];
		int[][] wid = new int[N][M];
		int result[] = new int[4];
		for (int i = 0; i < N ; i++) {
			for (int j = 0; j < M; j++) {
				if(j == M-1) len[i][j] = 1;
				int count = 1;
				int maxLen = 0;
				int minLen = 0;
				for (int k = 0; k < M-1-j; k++) {
					if (height[i][j+k] <= height[i][j+k+1] && k == 0) {
						minLen = height[i][j+k];
						maxLen = height[i][j+k+1];
					}
					if(height[i][j+k] > height[i][j+k+1] && k == 0) {
						minLen = height[i][j+k+1];
						maxLen = height[i][j+k];
					}
					if(height[i][j+k+1] > maxLen) {
						maxLen = height[i][j+k+1];
					}
					if(height[i][j+k+1] < minLen) {
						minLen = height[i][j+k+1];
					}
					if(maxLen - minLen <= C) count++;
					if(maxLen - minLen > C || j+k+1 == M-1) {
						len[i][j] = count;//count
						break;
					}
				}
			}
		}

		for(int j=0; j < M; j++) {
			for(int i = 0; i< N; i++) {
				int count = 1;
				int maxLen = 0;
				int minLen = 0;
				if (i==N-1) wid[i][j]=1;
				for(int k = 0; k < N-i-1; k++) {
					if (height[i+k][j] <= height[i+k+1][j] && k == 0) {
						minLen = height[i+k][j];
						maxLen = height[i+k+1][j];
					}
					if(height[i+k][j] > height[i+k+1][j] && k == 0) {
						minLen = height[i+k+1][j];
						maxLen = height[i+k][j];
					}
					if(height[i+k+1][j] > maxLen) {
						maxLen = height[i+k+1][j];
					}
					if(height[i+k+1][j] < minLen) {
						minLen = height[i+k+1][j];
					}
					if(maxLen-minLen <=C) count++;
					if(maxLen-minLen > C || i+k+1 == N-1) {
						wid[i][j] = count;
						break;
					}
				}
			}
		}

		//Compute the areas and ignore the violence in that area.
		for(int i = 0;  i < N; i++) {
			for (int j = 0; j < M; j++) {
				int minWid = Integer.MAX_VALUE;
				for (int k = j; k < j+len[i][j]; k++) {
					if(wid[i][k] < minWid) minWid = wid[i][k];
				}
				area[i][j] = minWid*len[i][j];
			}
		}

		//eliminate the violent area and then pick the maximum result.
		for(int i = 0; i < N; i++) {
			for (int j = 0; j< M; j++ ) {
				int hi = area[i][j]/len[i][j];
				int maxhi = Integer.MIN_VALUE;
				int minhi = Integer.MAX_VALUE;
				int k=i;
				int l=j;
				for(k=i; k < i + hi; k++) {
					for (l=j; l < j + len[i][j]; l++ ) {//k=i , l is not updated.
						if(height[k][l] > maxhi) maxhi = height[k][l];
						if(height[k][l] < minhi) minhi = height[k][l];
				}
			}
				if(maxhi - minhi <= C && area[i][j] > dif[maxhi-minhi]) {
					dif[maxhi-minhi] = area[i][j];
					for(int m = 0; m < C+1; m++) {
						if (dif[m] > max) {
							max=dif[m];
							result[0] = k-1 + 1;
							result[1] = l-1 + 1;
							result[2] = result[0] - hi + 1;
							result[3] = result[1] - len[i][j] + 1;
						}
					}
				}
		}
	}
		return result;
	}


	public static void main(String args[]) throws FileNotFoundException {
		int taskNum = Integer.parseInt(args[0]);
		//File srcFile = new File("config.txt");
		//int taskNum = 4;
		//System.out.println("Please input the configuration");
		Scanner input = new Scanner(System.in);
		String[] s1 = input.nextLine().split(" ");
		int n = Integer.parseInt(s1[0]);
		int m = Integer.parseInt(s1[1]);
		int c = Integer.parseInt(s1[2]);
		Skyscraper sky = new Skyscraper(n,m,c);
		//System.out.println("Please input the height");
		//Scanner input2 = new Scanner(System.in);
		for(int i = 0; i < n; i++) {
			s1 = input.nextLine().split(" ");
			for(int j = 0; j < m; j++) {
				sky.height[i][j] = Integer.parseInt(s1[j]);
			}
		}
		switch (taskNum) {
		case 1:
			int[] ans = new int[4];
			ans = sky.task1();
			System.out.println(ans[2] + " " + ans[3] + " " + ans[0] + " " + ans[1]);
			break;
		case 2:
			int[] ans2 = new int[4];
			ans2 = sky.task2();
			System.out.println(ans2[2] + " " + ans2[3] + " " + ans2[0] + " " + ans2[1]);
			break;
		case 3:
			int[] ans3 = new int[4];
			ans3 = sky.task3();
			System.out.println(ans3[2] + " " + ans3[3] + " " + ans3[0] + " " + ans3[1]);
			break;
		case 4:
			int[] ans4 = new int[4];
			ans4 = sky.task4();
			System.out.println(ans4[2] + " " + ans4[3] + " " + ans4[0] + " " + ans4[1]);
		}
		input.close();
		//input2.close();
	}
}
