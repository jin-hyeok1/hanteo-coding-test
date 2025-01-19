package kr.personal.jh.todo2;

import java.util.Arrays;

public class CoinCombination {

    private final Case[] cases;

    private CoinCombination(Case[] cases) {
        this.cases = cases;
    }

    public static CoinCombination problemCase(Case... cases) {
        if (cases == null || cases.length == 0) {
            throw new IllegalArgumentException("cases cannot be null or empty");
        }
        return new CoinCombination(cases);
    }

    public int[] solve() {
        return Arrays.stream(cases).mapToInt(Case::solve).toArray();
    }

    public static class Case {

        private int sum;
        private int[] coins;
        private int result;
        private Case() {
            this.result = 0;
        }

        private int solve() {
            backTracking(0, 0);
            return result;
        }

        private void backTracking(int coinSum, int startIdx) {
            if (coinSum == sum) {
                result++;
                return;
            }

            for (int i = startIdx; i < coins.length; i++) {
                if (coinSum + coins[i] > sum) break;
                backTracking(coinSum + coins[i], i);
            }
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private final Case instance;
            private Builder() {
                this.instance = new Case();
            }

            public Builder sum(int sum) {
                instance.sum = sum;
                return this;
            }

            public Builder coins(int... coins) {
                if (coins == null || coins.length == 0) {
                    throw new IllegalArgumentException("coins cannot be null or empty");
                }
                Arrays.sort(coins);
                instance.coins = coins;
                return this;
            }

            public Case build() {
                return instance;
            }
        }

    }
}
