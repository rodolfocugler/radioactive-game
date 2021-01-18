package br.com.rodolfocugler.repositories;

import br.com.rodolfocugler.domains.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface TransportRepository extends JpaRepository<Transport, Long> {
  Transport findFirstByAccountGroup_IdAndCarIndexOrderByTimestampDesc(long accountGroupId, int carId);

  List<Transport> findAllByAccountGroup_IdOrderByTimestamp(long accountGroupId);
}