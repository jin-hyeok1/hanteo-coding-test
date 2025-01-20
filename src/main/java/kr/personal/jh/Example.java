package kr.personal.jh;

import kr.personal.jh.todo1.CategoryDatabase;
import kr.personal.jh.todo1.StructuredData;
import kr.personal.jh.todo2.CoinCombination;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 테스트를 위해 사용한 코드이며 method 사용의 이해를 돕기 위한 {@code class}입니다.
 */
public class Example {
    public void useExampleOfCategoryProblem() {
        CategoryDatabase categoryDatabase = new CategoryDatabase();
        categoryDatabase.saveAll(
                CategoryDatabase.Category.builder() // index: 0
                        .name("남자")
                        .build(),
                CategoryDatabase.Category.builder() // index: 1
                        .name("엑소")
                        .build(),
                CategoryDatabase.Category.builder() // index: 2
                        .name("공지사항")
                        .build(),
                CategoryDatabase.Category.builder() // index: 3
                        .name("첸")
                        .build(),
                CategoryDatabase.Category.builder() // index: 4
                        .name("백현")
                        .build(),
                CategoryDatabase.Category.builder() // index: 5
                        .name("방탄소년단")
                        .build(),
                CategoryDatabase.Category.builder() // index: 6
                        .name("뷔")
                        .build(),
                CategoryDatabase.Category.builder() // index: 7
                        .name("여자")
                        .build(),
                CategoryDatabase.Category.builder() // index: 8
                        .name("블랙핑크")
                        .build(),
                CategoryDatabase.Category.builder() // index: 9
                        .name("로제")
                        .build(),
                CategoryDatabase.Category.builder() // index: 10
                        .name("익명게시판")
                        .build()
        );
        categoryDatabase.defineRelation(0, 1)
                .defineRelation(1, 2)
                .defineRelation(1, 3)
                .defineRelation(1, 4)
                .defineRelation(0, 5)
                .defineRelation(5, 6)
                .defineRelation(7, 8)
                .defineRelation(8, 9)
                .defineRelation(1, 10)
                .defineRelation(5, 10)
                .defineRelation(8, 10);
        StructuredData<String, CategoryDatabase.Category> structuredCategory = new StructuredData<>(categoryDatabase);
        String fullJsonText = structuredCategory.findByKey("남자")
                .getFullJsonText((Comparator.comparingInt(o -> ((CategoryDatabase.Category) o).getId())));
        System.out.println(fullJsonText);
    }

    public void useExampleOfCoinCombination() {
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
