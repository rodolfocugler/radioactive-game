package br.com.rodolfocugler.services;

import br.com.rodolfocugler.domains.Transport;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.repositories.ToolRepository;
import br.com.rodolfocugler.repositories.TransportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportService {

  public TransportService(TransportRepository transportRepository,
                          ToolRepository toolRepository) {
    this.transportRepository = transportRepository;
    this.toolRepository = toolRepository;
  }

  private final TransportRepository transportRepository;
  private final ToolRepository toolRepository;

  public Transport get(long id) throws DataNotFoundException {
    return transportRepository
            .findById(id)
            .orElseThrow(() -> new DataNotFoundException("Transport was not found."));
  }

  public List<Transport> get() {
    return transportRepository.findAll();
  }

  public Transport add(Transport transport) {
    transport.getTools().forEach(tool -> toolRepository.save(tool));
    transportRepository.save(transport);
    return transport;
  }

  public Transport getByIndex(int carId, long accountGroupId) {
    return transportRepository
            .findFirstByAccountGroup_IdAndCarIndexOrderByTimestampDesc(accountGroupId, carId);
  }
}