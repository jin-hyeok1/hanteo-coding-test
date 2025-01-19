package kr.personal.jh;

import kr.personal.jh.todo2.CoinCombination;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] todo2Solved = CoinCombination.problemCase(
                CoinCombination.Case.builder()
                        .sum(10)
                        .coins(2, 5, 3, 6)
                        .build(),

                CoinCombination.Case.builder()
                        .sum(4)
                        .coins(1, 2, 3)
                        .build()
        ).solve();

        System.out.println(Arrays.toString(todo2Solved));

    }
}