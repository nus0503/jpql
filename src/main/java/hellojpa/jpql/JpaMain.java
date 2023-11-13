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

            em.flush();
            em.clear();

            String query = "select t from Team t join fetch t.members";
            List<Team> result = em.createQuery(query, Team.class)
                    .getResultList();
            int i = 0;
            for (Team team : result) {

                System.out.println("team = " + team.getName() + ", team = " + team);
                for (Member member1 : team.getMembers()) {
                    System.out.println("-> username = " + member1.getUsername() + ", member = " + member1);
                }
            }

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
