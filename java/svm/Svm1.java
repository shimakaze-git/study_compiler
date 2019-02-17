// package svm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Stack;

// import svm.Alu;

/*
    後置記法で表現された式を計算するクラス
    ->
    メモリ上にロードしたプログラム（式）を先頭から順に読み込んで実行（評価）
    ->

    どこまでプログラムを実行したかを記録しながら処理を進める必要があり、
    そこで、int型のフィールドpcをプログラムカウンタとして用意

    load() :
        プログラムを仮想スタックマシンへロードするメソッド
    execute() :
        ロードしたプログラムを実行するメソッド
    executeCommand() :
        命令の判定を実行するメソッド。execute()メソッドから呼び出される。
*/
public class Svm1 {
    /*
        private
    */

    private byte[] code = new byte[256];
    // プログラムコードを買う脳したbyte型の配列

    private int codeLength = 0;
    // 

    private Stack<Byte> operandStack = new Stack<Byte>();
    // オペランドスタック

    // private Alu alu = new Alu();
    private Alu alu;
    //  演算処理をしてくれるクラス

    private int pc; //  プログラムカウンタ

    // プログラムを仮想スタックマシンへロード
    public void load(String fileName) throws IOException {
        FileInputStream is = null;
        try {
            // FileInputStreamを使ってバイトデータを読み込む
            is = new FileInputStream(new File(fileName));
            int len = 0;
            
            // readメソッドで一度に読み込むbyte数は最大8bytes
            // 読み込んだバイトデータはフィールドcodeに保存されるように指定
            // 読み込んだバイトデータの長さはcodeLengthに記録
            while ((len = is.read(code, len, 8)) != -1) {
                codeLength += len;
            }

        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // ロードしたプログラムを実行
    public void execute() {
        alu = new Alu();
        for (pc = 0; pc < codeLength; pc++){
            // pcを1ずつ増加させてexecuteCommandメソッドを呼び出す
            executeCommand(code[pc]);
        }
    }

    // 命令の判定を実行
    public void executeCommand(byte command){
        // パラメータcommandに渡された
        // バイトコードの値によって実行する処理が変わる

        byte a;
        byte b;

        switch (command) {
            //  続くコードにある値をオペランドスタックへ積む処理（push）
            case 16:
                // pc+1の値をpushする
                operandStack.push(code[++pc]);
                break;
            // オペランドスタックに積まれている値を2つ取り出すpop）
            // それぞれの値を加算した結果をオペランドスタックへ積む
            case 96:
                // スタックから二つの値を取り出す
                b = operandStack.pop();
                a = operandStack.pop();

                // 加算処理をして結果を再度スタックに格納
                operandStack.push(alu.add(a, b));
                break;
            // オペランドスタックに積まれている値を2つ取り出すpop）
            // それぞれの値を乗算した結果をオペランドスタックへ積む
            case 104:
                // スタックから二つの値を取り出す
                b = operandStack.pop();
                a = operandStack.pop();

                // 乗算処理をして結果を再度スタックに格納
                operandStack.push(alu.multiply(a, b));
                break;
                // オペランドスタックの一番上に積まれている値をコンソール画面へ出力
            case -48:
                System.out.print(operandStack.peek());
                break;
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Svm1 <svm file>");
        } else {
            String fileName = args[0];
            Svm1 svm1 = new Svm1();

            try {
                svm1.load(fileName);
                svm1.execute();
            } catch (IOException e) {
                System.out.println("error");
                System.out.println(e);
            }
        }
    }
}
