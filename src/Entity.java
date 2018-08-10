import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;

public class Entity {

    String str = "";
    Double distance = Double.MAX_VALUE;
    Double fitness = 0d;

    Entity(int len){
        Random r = new Random();
        int symEnumSize = Main.AllowedSymbols.length;

        for(int i=0; i< len; ++i) {
            str += Main.AllowedSymbols[r.nextInt(symEnumSize)];
        }
    }

    Entity(Entity father, Entity mother){
        Random r = new Random();

        int halfLen = father.str.length()/2;                    // полудлина – количество генов которые я беру от одного из родителей
        ArrayList<Integer> crossPositions = new ArrayList<>();  // позиции этих генов
        char[] childTempStr = father.str.toCharArray();         // строка ребенка
        char[] motherTempStr = mother.str.toCharArray();        // строка матери


        for(int i=0; i < father.str.length(); ++i)  // загоняю в массив позиций все позиции отца
            crossPositions.add(i);

        for(int i=0; i < halfLen; ++i)              // выбрасываю случайную половину из них
            crossPositions.remove(r.nextInt(crossPositions.size()));


        for(int i=0; i < crossPositions.size(); ++i)   // и на их место вставляю гены матери
            childTempStr[crossPositions.get(i)] = motherTempStr[crossPositions.get(i)];


        str = new String(childTempStr); // и сохраняю это все в потомка
    }

    void calculateFitness(String ideal){
        int len = ideal.length();
        int matches = 0;

        distance = 0d;

        for(int i=0; i < len; ++i){

            int difference = Main.AllowedSymbolsString.indexOf(str.charAt(i))
                    - Main.AllowedSymbolsString.indexOf(ideal.charAt(i));

            if(difference == 0) matches++;

            distance += abs(difference);
            // Получается что я буду каждый раз делать поиск символа, не круто
            // TODO перегнать инты идеала в массив
        }


        fitness = (1d - distance/(1d*len)/(1d*Main.AllowedSymbolsLength));
        fitness *= (1d*matches)/(1d*len);

    }

    double getDistance(){return distance;}
    double getFitness() {return fitness;}

    void mutate(){
        char[] tempStr = str.toCharArray(); // строку в чар
        int mutatePos = new Random().nextInt(str.length()); // позиция мутации
        int mutateGene = Main.AllowedSymbolsString.indexOf(tempStr[mutatePos]); // ген который буду мутировать в виде его номера в массиве

        int radius = new Random().nextInt(3) + 1;   // радиус мутации
        radius *= (new Random().nextBoolean()) ? 1 : -1;    // куда мутируем

        mutateGene += radius;

        if(mutateGene < 0)                          mutateGene += Main.AllowedSymbolsLength;
        if(mutateGene >= Main.AllowedSymbolsLength) mutateGene -= Main.AllowedSymbolsLength;

        tempStr[mutatePos] = Main.AllowedSymbols[mutateGene];

        str = new String(tempStr);
    }
}
