import java.io.BufferedReader;     
import java.io.InputStreamReader;  
import java.util.*;                

public class Main {

    static class Edge {  // 接続先の駅と距離を表すクラス
        int destination; // 行き先の頂点
        double distance; // 辺の距離

        Edge(int destination, double distance) { 
            this.destination = destination;    
            this.distance = distance;          
        }
    }

    private static final Map<Integer, List<Edge>> graph = new HashMap<>(); // 行き先と距離を保持するグラフ
    private static double longestDistance = 0.0;                           // 最長距離
    private static List<Integer> longestPath = new ArrayList<>();          // 最長経路を保持するリスト

    public static void main(String[] args) throws Exception {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); // 標準入力から読み込むためのBufferedReader
        String line;                                                                  // 入力された1行分の文字列

        while ((line = reader.readLine()) != null) { // 入力が終わるまで1行ずつ読み込む

            line = line.trim();   // 前後の空白を削除
            if (line.isEmpty()) { // 空行の場合はスキップ
                continue;
            }

            String[] values = line.split(",");                      // 始点、終点、距離に分割
            int from = Integer.parseInt(values[0].trim());          // 始点をint型に変換
            int to = Integer.parseInt(values[1].trim());            // 終点をint型に変換
            double distance = Double.parseDouble(values[2].trim()); // 距離をdouble型に変換

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(new Edge(to, distance)); // 始点から終点への辺を追加
            graph.computeIfAbsent(to, k -> new ArrayList<>()).add(new Edge(from, distance)); // 終点から始点への辺を追加
        }

        for (Integer start : graph.keySet()) { // グラフの各頂点を始点として探索

            Set<Integer> visited = new HashSet<>(); // 訪問済み頂点を保持するセット
            List<Integer> path = new ArrayList<>(); // 現在の経路を保持するリスト

            visited.add(start); // 始点を訪問済みに追加
            path.add(start);    // 始点を経路に追加

            dfs(start, visited, path, 0.0); // 深さ優先探索を開始
        }

        for (Integer vertex : longestPath) { // 最長経路の各頂点を出力
            System.out.println(vertex);
        }
    }
 
    // current:現在の駅、visited:訪問済み駅、currentPath:現在の経路、currentDistance:現在までの総距離
    private static void dfs(int current, Set<Integer> visited, List<Integer> currentPath, double currentDistance) {

        if (currentDistance > longestDistance) {         // 現在の距離が最長距離よりも長い場合
            longestDistance = currentDistance;           // 最長距離を更新
            longestPath = new ArrayList<>(currentPath);  // 最長経路を更新
        }

        for (Edge edge : graph.getOrDefault(current, Collections.emptyList())) { // 現在の駅と接続している駅を順番に探索

            if (visited.contains(edge.destination)) { // すでに訪問済みの駅の場合はスキップ
                continue;
            }

            visited.add(edge.destination);     // 訪問済みに追加
            currentPath.add(edge.destination); // 経路に追加

            dfs(edge.destination, visited, currentPath, currentDistance + edge.distance);  // 再帰的に探索

            currentPath.remove(currentPath.size() - 1); // 経路から最後の駅を削除
            visited.remove(edge.destination); // 訪問済みから削除
        }
    }
}