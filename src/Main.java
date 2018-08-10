import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

public class Main {

  public static final char[] AllowedSymbols = {
          'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', 'g', 
          'G', 'h', 'H', 'i', 'I', 'j', 'J', 'k', 'K', 'l', 'L', 'm', 'M', 
          'n', 'N', 'o', 'O', 'p', 'P', 'q', 'Q', 'r', 'R', 's', 'S', 't',
          'T', 'u', 'U', 'v', 'V', 'w', 'W', 'x', 'X', 'y', 'Y', 'z', 'Z',
          ' ', ',', '.', '?', '!', ';', '\'', ':', '&' };
  public static final String AllowedSymbolsString = new String(AllowedSymbols);
  public static final int AllowedSymbolsLength = AllowedSymbols.length;

  static String ideal = "There is nothing either good or bad but thinking makes it so.";

  public static void main (String[] args) {

    int populationSize = 1000;
    int elite = (int) (0.01 * populationSize);
    double mutationsPerGenRatio = 0.001;
    int maxGens = 2500;

    String inputParameter = "";
    Scanner in = new Scanner(System.in);
    String oneMoreTime = "y";

    while (oneMoreTime.equals("y")) {
      while (!inputParameter.equals("0")) {
        System.out.println("Current settings: \n" +
                "[1] Ideal line: " + ideal + "\n" +
                "[2] Population size: " + populationSize + "\n" +
                "[3] Elite size: " + elite + "\n" +
                "[4] Gene mutation probability: " + mutationsPerGenRatio + "\n" +
                "[5] Maximum number of generations: " + maxGens + "\n" +
                "Enter [0] to start simulation or [1-5] to change settings\n");

        inputParameter = in.nextLine();

        switch (inputParameter) {
          case "0": {
            System.out.println("Let's go");
            break;
          }
          case "1": {
            System.out.print("New ideal line: ");
            ideal = in.nextLine();
            break;
          }
          case "2": {
            System.out.print("New population size: ");
            populationSize = in.nextInt();
            in.nextLine(); // чтобы скушать переход на следующую строку
            break;
          }
          case "3": {
            System.out.print("New number of elite entities: ");
            elite = in.nextInt();
            in.nextLine();
            break;
          }
          case "4": {
            System.out.print("New gene mutation probability: ");
            mutationsPerGenRatio = in.nextDouble();
            in.nextLine();
            break;
          }
          case "5": {
            System.out.print("New maximum number of generations: ");
            maxGens = in.nextInt();
            in.nextLine();
            break;
          }
          default:
            System.out.println("Enter 0-5");
        }
      }


      int mating = populationSize - elite;
      int proportionalMass = populationSize * 100; // Размер пропорциональной популяции
      int mutationsPerGen = (int) (mutationsPerGenRatio * populationSize * ideal.length());

      double bestMatch;
      int generation = 0;

      ArrayList<Entity> population = new ArrayList<>();
      ArrayList<Entity> proportionalPopulation;

      Random r = new Random();


      System.out.println("Ideal: \n" + ideal + "\n");

      for (int i = 0; i < populationSize; ++i) {
        population.add(new Entity(ideal.length()));
        population.get(i).calculateFitness(ideal);
      }

      population.sort(Comparator.comparing(Entity::getDistance, (e1, e2) -> {
        return e1.compareTo(e2);
      }));
      bestMatch = population.get(0).fitness;

      // пока не найду идеал
      while (bestMatch < 1d && generation < maxGens) {
        double populationWeight = 0d;
        ArrayList<Entity> newPopulation = new ArrayList<>();
        proportionalPopulation = new ArrayList<>();

        // Вывожу лучшее попадание
        if (generation % 50 == 0)
          System.out.println("Generation " + generation + " best match: " + bestMatch
                  + "\n" + population.get(0).str + "\n");

        // Считаю вес популяции -- сумму фитнессов от каждой особи
        for (int i = 0; i < populationSize; ++i) {
          populationWeight += population.get(i).fitness;
        }

        // Создаю пропорциональную популяцию
        // Особь попадает в нее пропорциональное своему фитнессу количество раз
        for (int i = 0; i < populationSize; ++i) {
          int entityProportionalWeight = (int) (population.get(i).fitness / populationWeight * proportionalMass);

          for (int j = 0; j < entityProportionalWeight; ++j)
            proportionalPopulation.add(population.get(i));
        }

        // спаривание
        for (int i = 0; i < mating; ++i) {
          newPopulation.add(new Entity(proportionalPopulation.get(r.nextInt(proportionalPopulation.size())),
                  proportionalPopulation.get(r.nextInt(proportionalPopulation.size()))));
        }

        // мутации
        for (int i = 0; i < mutationsPerGen; ++i) {
          newPopulation.get(r.nextInt(mating)).mutate();
        }

        // элита
        for (int i = 0; i < elite; ++i) {
          newPopulation.add(population.get(i));
        }

        // перерасчёт значений фитнесс-функции
        for (int i = 0; i < newPopulation.size(); ++i) {
          newPopulation.get(i).calculateFitness(ideal);
        }

        newPopulation.sort(Comparator.comparing(Entity::getDistance, (e1, e2) -> {
          return e1.compareTo(e2);
        }));

        bestMatch = newPopulation.get(0).fitness;

        population = newPopulation;

        generation++;
      }

      // вывожу финальный результат
      System.out.println("Generation " + generation + " best match: " + bestMatch
              + "\n" + population.get(0).str + "\n");

      if(bestMatch <1d )
          System.out.println("The ideal was: \n" + ideal + "\n");

      System.out.println("Еще разок? [y]/[n]");
      oneMoreTime = in.nextLine();
      inputParameter = "";
    }
  }
}



















