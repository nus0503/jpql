package hellojpa.jpql;



import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;



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
            Member member = new Member();
            member.setUsername("member1");
            em.persist(member);

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
