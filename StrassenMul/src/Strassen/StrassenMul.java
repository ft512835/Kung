package Strassen;

/*第4章 分治法  Strassen矩阵乘法*/

public class StrassenMul {

    //该程序可以对两个同阶的2^n阶的矩阵采用Strassen算法做矩阵乘法
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int[][] a = {
                    {1,3,1,4},
                    {4,1,1,0},
                    {0,1,3,0},
                    {5,0,2,1}
                    };
        
        int[][] b = {
                    {0,1,5,1},
                    {2,1,0,4},
                    {2,0,1,1},
                    {1,3,5,0}
                    };
            
        int[][] result = StrassenMul(a,b);
        System.out.println("输出矩阵：");   
        for(int i = 0;i < result.length;i++)
        {
            for(int j = 0;j < result.length;j++)
                System.out.print(result[i][j] + "  ");
            System.out.println();
        }        
    }
    
    
    public static int[][] StrassenMul(int[][] a,int[][] b){    //a，b均是2的乘方的方阵
        int[][] result = new int[a.length][a.length];
        if(a.length == 2)        //如果a,b均是2阶的，递归结束条件
            result = StrassMul(a,b);
        else                    //否则（即a，b都是4,8,16...阶的）
        {
            //a的四个子矩阵
            int[][] A00 = copyArrays(a,1);
            int[][] A01 = copyArrays(a,2);
            int[][] A10 = copyArrays(a,3);
            int[][] A11 = copyArrays(a,4);
            //b的四个子矩阵
            int[][] B00 = copyArrays(b,1);
            int[][] B01 = copyArrays(b,2);
            int[][] B10 = copyArrays(b,3);
            int[][] B11 = copyArrays(b,4);
            
            //递归调用
            int[][] m1 = StrassenMul(addArrays(A00,A11),addArrays(B00,B11));
            int[][] m2 = StrassenMul(addArrays(A10,A11),B00);
            int[][] m3 = StrassenMul(A00,subArrays(B01,B11));
            int[][] m4 = StrassenMul(A11,subArrays(B10,B00));
            int[][] m5 = StrassenMul(addArrays(A00,A01),B11);
            int[][] m6 = StrassenMul(subArrays(A10,A00),addArrays(B00,B01));
            int[][] m7 = StrassenMul(subArrays(A01,A11),addArrays(B10,B11));
            
            //得到result的四个子矩阵
            int[][] C00 = addArrays(m7,subArrays(addArrays(m1,m4),m5));//m1+m4-m5+m7
            int[][] C01 = addArrays(m3,m5);    //m3+m5
            int[][] C10 = addArrays(m2,m4);    //m2+m4
            int[][] C11 = addArrays(m6,subArrays(addArrays(m1,m3),m2));//m1+m3-m2+m6
            
            //也可以按照下列方法来求C
            //C00 = addArrays(StrassenMul(A00,B00),StrassenMul(A01,B10));
            //C01 = addArrays(StrassenMul(A00,B01),StrassenMul(A01,B11));
            //C10 = addArrays(StrassenMul(A10,B00),StrassenMul(A11,B10));
            //C11 = addArrays(StrassenMul(A10,B01),StrassenMul(A11,B11));
            
            //将四个子矩阵合并成result
            Merge(result,C00,1);
            Merge(result,C01,2);
            Merge(result,C10,3);
            Merge(result,C11,4);
        }
        return result;
    }
    
    
    private static void Merge(int[][] result,int[][] C,int flag){
        //将C复制到result的相应位置
        switch(flag)
        {
            case 1:
                for(int i = 0;i < result.length/2;i++)
                    for(int j = 0;j < result.length/2;j++)
                    result[i][j] = C[i][j];
                break;
            case 2:
                for(int i = 0;i < result.length/2;i++)
                    for(int j = result.length/2;j < result.length;j++)
                        result[i][j] = C[i][j-result.length/2];
                break;
            case 3:
                for(int i = result.length/2;i < result.length;i++)
                    for(int j = 0;j < result.length/2;j++)
                        result[i][j] = C[i - result.length/2][j];
                break;
            case 4:
                for(int i = result.length/2;i < result.length;i++)
                    for(int j = result.length/2;j < result.length;j++)
                        result[i][j] = C[i - result.length/2][j-result.length/2];
                break;
        }
    }
    
    
    private static int[][] copyArrays(int[][] a,int flag){
        //得到分割矩阵的子矩阵
        int[][] result = new int[a.length/2][a.length/2];
        switch(flag)
        {
            case 1:
                for(int i = 0;i < a.length/2;i++)
                    for(int j = 0;j < a.length/2;j++)
                        result[i][j] = a[i][j];
                break;
            case 2:
                for(int i = 0;i < a.length/2;i++)
                    for(int j = a.length/2;j < a.length;j++)
                        result[i][j-a.length/2] = a[i][j];
                break;    
            case 3:
                for(int i = a.length/2;i < a.length;i++)
                    for(int j = 0;j < a.length/2;j++)
                        result[i - a.length/2][j] = a[i][j];
                break;    
            case 4:
                for(int i = a.length/2;i < a.length;i++)
                    for(int j = a.length/2;j < a.length;j++)
                        result[i-a.length/2][j-a.length/2] = a[i][j];
                break;
        }
        
        return result;
    }
    
    
    private static int[][] StrassMul(int[][] a,int[][] b){
        //计算2个二阶的矩阵乘法
        //Strassen方法使用了7次乘法，18次加法（传统方法是8次乘法4次加法）
        int[][] result = new int[2][2];
        
        int m1 = (a[0][0] + a[1][1]) * (b[0][0] + b[1][1]);
        int m2 = (a[1][0] + a[1][1]) * b[0][0];
        int m3 = a[0][0] * (b[0][1] - b[1][1]);
        int m4 = a[1][1] * (b[1][0] - b[0][0]);
        int m5 = (a[0][0] + a[0][1]) * b[1][1];
        int m6 = (a[1][0] - a[0][0]) * (b[0][0] + b[0][1]);
        int m7 = (a[0][1] - a[1][1]) * (b[1][0] + b[1][1]);
        
        result[0][0] = m1 + m4 - m5 + m7;
        result[0][1] = m3 + m5;
        result[1][0] = m2 + m4;
        result[1][1] = m1 + m3 - m2 + m6;
        
        return result;
    }
    
    
    private static int[][] addArrays(int[][] a,int[][] b){
        //求2个同阶矩阵的和
        int[][] result = new int[a.length][a.length];
        //System.out.println(result.length);
        for(int i = 0;i < result.length;i++)
            for(int j = 0;j < result.length;j++)
            //for(int j = 0;i < result.length;j++)
                    result[i][j] = a[i][j] + b[i][j];
        return result;
    }
    
    
    private static int[][] subArrays(int[][] a,int[][] b){
        //矩阵减法
        int[][] result = new int[a.length][a.length];
        for(int i = 0;i < result.length;i++)
            for(int j = 0;j < result.length;j++)
            //for(int j = 0;i < result.length;j++)
                result[i][j] = a[i][j] - b[i][j];
        return result;
    }
    

}