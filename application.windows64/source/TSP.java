import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class TSP extends PApplet {



class Point
{
    int x;
    int y;

    Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void show()
    {
        stroke(0xff4aaeed);
        strokeWeight(2);
        noFill();
        ellipse(this.x, this.y, 20, 20);
    }
}

Point[] population;
int no_of_cities = 12;
// int x;
// int order[] = new int[]{0, 1, 2, 3, 4};
ArrayList<ArrayList<Integer>> allorders = new ArrayList<ArrayList<Integer>>();
int all_order_size = 90000;
// ArrayList<Integer> bestOrder;
// int best = Integer.MAX_VALUE;

public void setup() {
    
    frameRate(6);
    population = new Point[no_of_cities];
    for(int i=0; i<no_of_cities; i++)
    {
        int x = (int) (Math.random()*width);
        int y = (int) (Math.random()*height/2);
        population[i] = new Point(x,y);
    }
    // generateBruteOrder(no_of_cities);
    generateShuffledOrder(no_of_cities);
    // x = allorders.size()-1;
    // bestOrder = new ArrayList<Integer>(allorders.get(0));
    // for(int i=0; i<allorders.size(); i++)
    //     println(allorders.get(i));
}

public void draw() {
    background(0);
    for(int i=0; i<no_of_cities; i++)
    {
        population[i].show();
    }
    translate(0, 450);
    for(int i=0; i<no_of_cities; i++)
    {
        population[i].show();
    }
    translate(0, -450);

    constructForGA();

    // if(x>=0)
    // {
    //     drawLine(allorders.get(x));
    //     translate(0, 450);
    //     calculateBestEver(allorders.get(x));
    //     // println(best);
    //     x--;
    // }
    // else 
    // {
    //     translate(0, 450);
    //     drawBestLine(besEverOrder);
    //     noLoop();
    // }
}

public void drawLine(ArrayList<Integer> order)
{
    for(int i=0; i<no_of_cities-1; i++)
    {
        int cityAindex = order.get(i);
        int cityBindex = order.get(i+1);
        int x1 = population[cityAindex].x;
        int y1 = population[cityAindex].y;
        int x2 = population[cityBindex].x;
        int y2 = population[cityBindex].y;
        stroke(255, 0, 0);
        strokeWeight(3);
        line(x1, y1, x2, y2);
    }
}

public void drawBestLine(ArrayList<Integer> order)
{
    for(int i=0; i<no_of_cities-1; i++)
    {
        int cityAindex = order.get(i);
        int cityBindex = order.get(i+1);
        int x1 = population[cityAindex].x;
        int y1 = population[cityAindex].y;
        int x2 = population[cityBindex].x;
        int y2 = population[cityBindex].y;
        stroke(0xffFFFFFF);
        strokeWeight(7);
        line(x1, y1, x2, y2);
    }
}


// void generateBruteOrder(int n)
// {
//     ArrayList<Integer> list = new ArrayList<Integer>();
//     for(int i=0; i<n; i++)
//         list.add(i);
//     permutate(list, 0);
// }

// void permutate (ArrayList<Integer> list, int k)
// {
//     if(k==list.size()-1)
//     {
//         println("a "+list);
//         allorders.add(new ArrayList<Integer>(list));
//         return;
//     }
//     for(int i=k; i<list.size(); i++)
//     {
//         swap(list, k, i);
//         permutate(list, k+1);
//         swap(list, i, k);
//     }
// }

public void swap(ArrayList<Integer> list, int a, int b)
{
    int temp = list.get(a);
    list.set(a, list.get(b));
    list.set(b, temp);
}   

// void calculateBestEver(ArrayList<Integer> order)
// {
//     int score = calculateScore(order);
//     if(score<best)
//     {
//         best = score;
//         bestOrder = new ArrayList<Integer>(order);
//     }
//     drawBestLine(bestOrder);
// }

public int calculateScore(ArrayList<Integer> order)
{
    int sum = 0;
    for(int i=0; i<order.size()-1; i++)
    {
        int cityAindex = order.get(i);
        int cityBindex = order.get(i+1);
        int x1 = population[cityAindex].x;
        int y1 = population[cityAindex].y;
        int x2 = population[cityBindex].x;
        int y2 = population[cityBindex].y;
        sum += (int) dist(x1, y1, x2, y2);
    }
    return sum;
}
// find fitness - done
// normalize the fitness - done
// create mechanism to pick for crossover and mutation according to fitness - done
// create new generation:
    // crossover
    // mutate
int mod = 1000000007;

float[] fitness = new float[all_order_size];
float fitness_sum = 0;
float bestFitness = -1.0f;
int bestScore = Integer.MAX_VALUE;
ArrayList<Integer> bestOrder = new ArrayList<Integer>();

int bestTillNow = Integer.MAX_VALUE;
ArrayList<Integer> bestTillNowOrder = new ArrayList<Integer>();

public void constructForGA()
{
    findFitness();
    normalizeFitness();
    generate();

    println(bestOrder);

    drawLine(bestOrder);
    translate(0, 450);
    calculateBestTillNow();
    drawBestLine(bestTillNowOrder);
}

public void findFitness()
{
    bestScore = Integer.MAX_VALUE;
    bestOrder.clear();
    for(int i=0; i<all_order_size; i++)
    {
        int d = calculateScore(allorders.get(i));
        if(d<bestScore)
        {
            bestScore = d;
            bestOrder = new ArrayList<Integer>(allorders.get(i));
        }
        fitness[i] = 100000.0f/((float)d+1.0f);
        fitness[i] *= fitness[i];

        fitness_sum += fitness[i];
    }   
}

public void normalizeFitness()
{
    for(int i=0; i<all_order_size; i++)
    {
        fitness[i] /= fitness_sum;
        bestFitness = Math.max(bestFitness, fitness[i]);
        // println("best fitness: "+bestFitness);
    }
}

public ArrayList<Integer> pick()
{
    int counter = 100000;
    while(counter-->0)
    {
        int index = (int) (Math.random()*all_order_size);
        float r = (float) (Math.random()*bestFitness);
        if(fitness[index]>=r)
            return allorders.get(index);
    }
    return null;
}

public ArrayList<Integer> crossover(ArrayList<Integer> order1, ArrayList<Integer> order2)
{
    int r = (int) (Math.random()*order1.size());
    ArrayList<Integer> neworder = new ArrayList<Integer>();

    HashSet<Integer> set = new HashSet<Integer>();
    for(int i=0; i<r; i++)
    {
        neworder.add(order1.get(i));
        set.add(order1.get(i));
    }
    for(int i=0; i<order2.size(); i++)
    {
        if(set.contains(order2.get(i)))
            continue;
        neworder.add(order2.get(i));
    }
    return neworder;
}

public void mutate(ArrayList<Integer> order)
{
    int index = (int) (Math.random()*(order.size()-2));
    swap(order, index, index+1);
}

public void generate()
{
    ArrayList<ArrayList<Integer>> newallorders = new ArrayList<ArrayList<Integer>>();
    for(int i=0; i<all_order_size; i++)
    {
        ArrayList<Integer> order1 = new ArrayList<Integer>(pick());
        // ArrayList<Integer> order2 = new ArrayList<Integer>(pick());
        // ArrayList<Integer> neworder = 
        //         new ArrayList<Integer>(crossover(order1, order2));
        mutate(order1);
        newallorders.add(order1);
    }
    allorders = newallorders;
}

public void generateShuffledOrder(int n)
{
    ArrayList<Integer> list = new ArrayList<Integer>();
    for(int i=0; i<n; i++)
        list.add(i);
    allorders.clear();
    for(int i=0; i<all_order_size; i++)
    {
        Collections.shuffle(list);
        allorders.add(list);
    }
}

public void calculateBestTillNow()
{
    if(bestScore<bestTillNow)
    {
        bestTillNow = bestScore;
        bestTillNowOrder = new ArrayList<Integer>(bestOrder);
    }
}
  public void settings() {  size(900, 900); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "TSP" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
