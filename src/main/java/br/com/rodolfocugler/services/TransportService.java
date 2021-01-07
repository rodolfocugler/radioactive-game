package br.com.rodolfocugler.services;

import br.com.rodolfocugler.domains.Transport;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.repositories.TransportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportService {

  public TransportService(TransportRepository transportRepository) {
    this.transportRepository = transportRepository;
  }

  private final TransportRepository transportRepository;

  public Transport get(long id) throws DataNotFoundException {
    return transportRepository
            .findById(id)
            .orElseThrow(() -> new DataNotFoundException("Transport was not found."));
  }

  public List<Transport> get() {
    return transportRepository.findAll();
  }

  public Transport add(Transport transport) {
    transportRepository.save(transport);
    return transport;
  }

  public Transport getByIndex(long carId, long accountGroupId) {
    return transportRepository.findFirstByAccountGroup_IdAndCarIndexOrderByTimestampDesc(carId,
            accountGroupId);
  }
}