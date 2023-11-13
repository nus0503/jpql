package hellojpa.jpql;



import javax.persistence.*;
import java.util.Collection;
import java.util.List;


public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //영속성 컨텍스트(persistence context)에는 1차 캐시와 2차 캐시가 있는데
        //1차 캐쉬는 데이터베이스 한 트랙잭션 안에서만 사용이 가능하다.
        //2차 캐쉬는 애플리케이션 전체에서 공유하는 캐시다.
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member = new Member();
            member.setUsername("회원1");
            member.changeTeam(teamA);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.changeTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.changeTeam(teamB);
            em.persist(member3);
//            Member member = new Member();
//            member.setUsername("member1");
//            member.setAge(10);
//            em.persist(member);


            // 벌크 연산을 해도 flush가 자동 호출 된다.
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate(); //벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리를 날린다.
            System.out.println("resultCount = " + resultCount);
            em.clear();
            Member member1 = em.find(Member.class, member.getId());
            System.out.println(member1.getAge()); // -< 이것처럼 벌크 연산을 한 후 영속성 컨텍스트를 초기화하지 않으면 벌크 연산을 한 후의 데이터를 조회해도 반영이 안된다.
            //그래서 벌크 연산 후엔 영속성 컨텍스트를 초기화해라

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }
        emf.close();
    }
}
