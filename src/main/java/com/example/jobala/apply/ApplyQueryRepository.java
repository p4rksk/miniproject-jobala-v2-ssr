package com.example.jobala.apply;

import com.example.jobala._user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApplyQueryRepository {

    private final EntityManager em;

    public List<ApplyResponse.ApplyDTO> findApplyCompByUserId(int compId) {
        String q = """
                SELECT at.id, jot.jobopen_title, rt.resume_title, rt.name AS resume_name, rt.edu, jot.end_Time, at.state, rt.id, ut.name AS user_name
                FROM apply_tb at
                INNER JOIN jobopen_tb jot ON at.jobopen_id = jot.id
                INNER JOIN resume_tb rt ON rt.id = at.resume_id
                INNER JOIN user_tb ut ON jot.user_id = ut.id
                WHERE jot.user_id = ? AND at.role = 0;
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, compId);

        JpaResultMapper mapper = new JpaResultMapper();
        List<ApplyResponse.ApplyDTO> responseDTO = mapper.list(query, ApplyResponse.ApplyDTO.class);
        return responseDTO;
    }

    public List<ApplyResponse.ApplyDTO> findByUserId(int sessionUserId) {
        String q = """
                SELECT at.id, jt.jobopen_title, rt.resume_title, ut.name, rt.edu, jt.end_Time, at.state, rt.id
                FROM apply_tb at
                join user_tb ut on at.user_id = ut.id
                join resume_tb rt on at.resume_id = rt.id
                join jobopen_tb jt on at.jobopen_id = jt.id
                WHERE at.user_id = ? order by id desc;
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, sessionUserId);

        JpaResultMapper mapper = new JpaResultMapper();
        List<ApplyResponse.ApplyDTO> responseDTO = mapper.list(query, ApplyResponse.ApplyDTO.class);
        return responseDTO;
    }

    public List<ApplyResponse.ApplyDTO2> findJopOpenByUserId(int userId) {
        String q = """
                SELECT at.id, jot.jobopen_title, rt.resume_title, ut.compname, rt.edu, jot.end_Time, at.state, jot.id
                FROM apply_tb at
                INNER JOIN jobopen_tb jot ON at.jobopen_id = jot.id
                INNER JOIN resume_tb rt ON rt.id = at.resume_id
                INNER JOIN user_tb ut ON jot.user_id = ut.id
                WHERE rt.user_id = ? and at.role = 1;
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, userId);

        JpaResultMapper mapper = new JpaResultMapper();
        List<ApplyResponse.ApplyDTO2> responseDTO = mapper.list(query, ApplyResponse.ApplyDTO2.class);
        return responseDTO;
    }
}