package br.com.rodolfocugler.services;

import br.com.rodolfocugler.domains.Response;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.repositories.ResponseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

  public ResponseService(ResponseRepository responseRepository) {
    this.responseRepository = responseRepository;
  }

  private final ResponseRepository responseRepository;

  public Response get(long id) throws DataNotFoundException {
    return responseRepository
            .findById(id)
            .orElseThrow(() -> new DataNotFoundException("Response was not found."));
  }

  public List<Response> get() {
    return responseRepository.findAll();
  }

  public Response add(Response response) {
    responseRepository.save(response);
    return response;
  }

  public Response edit(long id, Response newResponse) throws DataNotFoundException {
    Response response = get(id);
    response.setAccount(newResponse.getAccount());
    response.setText(newResponse.getText());
    return responseRepository.save(response);
  }

  public void delete(long id) throws DataNotFoundException {
    Response response = get(id);
    responseRepository.delete(response);
  }
}