package pre.basecamp.demo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import pre.basecamp.demo.entity.Memo;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemoRepositoryTest {
    @Autowired
    private MemoRepository memoRepository;

    @Test
    public void testClass() {
        System.out.println(memoRepository.getClass().getName());
    }

    @Test
    public void testInsertDummies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample..."+i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    public void testSelect1() {
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);

        System.out.println("===================================");

        if(result.isPresent()) {
            Memo memo = result.get();
            System.out.println(memo);
        }

        /*
        Hibernate:
            select
                memo0_.mno as mno1_0_0_,
                memo0_.memo_text as memo_tex2_0_0_
            from
                tbl_memo memo0_
            where
                memo0_.mno=?
        ===================================
        Memo(mno=100, memoText=Sample...100)

        조회 전 쿼리 실행
        */
    }

    @Transactional
    @Test
    public void testSelect2() {
        Long mno = 100L;

        Memo memo = memoRepository.getOne(mno);

        System.out.println("===================================");

        System.out.println(memo);

        /*
        ===================================
        Hibernate:
            select
                memo0_.mno as mno1_0_0_,
                memo0_.memo_text as memo_tex2_0_0_
            from
                tbl_memo memo0_
            where
                memo0_.mno=?
        Memo(mno=100, memoText=Sample...100)

        조회 시 쿼리 실행
        */
    }

    @Test
    public void testUpdate() {
        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();

        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void testDelete() {
        Long mno = 100L;

        memoRepository.deleteById(mno);
    }

    @Test
    public void testPageDefault() {
        Pageable pageable = PageRequest.of(0,10, Sort.by("mno").descending());

        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println("---------------------------------");

        System.out.println("Total Pages: "+result.getTotalPages());

        System.out.println("Total Count: "+result.getTotalElements());

        System.out.println("Page Number: "+result.getNumber());

        System.out.println("Page Size: "+result.getSize());

        System.out.println("has next page?: "+result.hasNext());

        System.out.println("first page?: "+result.isFirst());

        System.out.println("Content Size: "+result.getContent().size());
    }

    @Test
    public void testQueryMethod() {
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);

        for(Memo memo: list) {
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethodWithPageable() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());

        Page<Memo> result = memoRepository.findByMnoBetween(70L, 80L, pageable);

        result.get().forEach(System.out::println);
    }
}