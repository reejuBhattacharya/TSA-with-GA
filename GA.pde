// find fitness - done
// normalize the fitness - done
// create mechanism to pick for crossover and mutation according to fitness - done
// create new generation:
    // crossover
    // mutate
int mod = 1000000007;

float[] fitness = new float[all_order_size];
float fitness_sum = 0;
float bestFitness = -1.0;
int bestScore = Integer.MAX_VALUE;
ArrayList<Integer> bestOrder = new ArrayList<Integer>();

int bestTillNow = Integer.MAX_VALUE;
ArrayList<Integer> bestTillNowOrder = new ArrayList<Integer>();

void constructForGA()
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

void findFitness()
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
        fitness[i] = 100000.0/((float)d+1.0);
        fitness[i] *= fitness[i];

        fitness_sum += fitness[i];
    }   
}

void normalizeFitness()
{
    for(int i=0; i<all_order_size; i++)
    {
        fitness[i] /= fitness_sum;
        bestFitness = Math.max(bestFitness, fitness[i]);
        // println("best fitness: "+bestFitness);
    }
}

ArrayList<Integer> pick()
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

ArrayList<Integer> crossover(ArrayList<Integer> order1, ArrayList<Integer> order2)
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

void mutate(ArrayList<Integer> order)
{
    int index = (int) (Math.random()*(order.size()-2));
    swap(order, index, index+1);
}

void generate()
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

void generateShuffledOrder(int n)
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

void calculateBestTillNow()
{
    if(bestScore<bestTillNow)
    {
        bestTillNow = bestScore;
        bestTillNowOrder = new ArrayList<Integer>(bestOrder);
    }
}
