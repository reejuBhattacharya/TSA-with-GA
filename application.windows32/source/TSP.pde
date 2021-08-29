import java.util.*;

class Point
{
    int x;
    int y;

    Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    void show()
    {
        stroke(#4aaeed);
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

void setup() {
    size(900, 900);
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

void draw() {
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

void drawLine(ArrayList<Integer> order)
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

void drawBestLine(ArrayList<Integer> order)
{
    for(int i=0; i<no_of_cities-1; i++)
    {
        int cityAindex = order.get(i);
        int cityBindex = order.get(i+1);
        int x1 = population[cityAindex].x;
        int y1 = population[cityAindex].y;
        int x2 = population[cityBindex].x;
        int y2 = population[cityBindex].y;
        stroke(#FFFFFF);
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

void swap(ArrayList<Integer> list, int a, int b)
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

int calculateScore(ArrayList<Integer> order)
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
