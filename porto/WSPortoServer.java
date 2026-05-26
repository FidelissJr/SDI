package porto;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface WSPortoServer {

    @WebMethod
    Integer cadastrar_navio(String descricao, Integer capacidade);

    @WebMethod
    void remover_navio(Integer id);

    @WebMethod
    String relatorio_navio();

    @WebMethod
    Integer cadastrar_carga(String descricao, Integer volume);

    @WebMethod
    void remover_carga(Integer id);

    @WebMethod
    String relatorio_carga();

    @WebMethod
    double embarcar(String descricao);

    @WebMethod
    String relatorio_embarque();

}
