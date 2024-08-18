package com.meusboleto.backend.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meusboleto.backend.model.Transaction;
import com.meusboleto.backend.repository.TransactionRepository;

@CrossOrigin
@RestController
public class TransactionREST {

    @Autowired
    private TransactionRepository repo;

    @Autowired
    private ModelMapper mapper;

    // @Autowired
    // private ProdutoRepository produtoService;

    @GetMapping(value = "/pedidos/", produces = "application/json;charset=UTF-8")
    public List<Transaction> listarTodos() {
        List<Transaction> lista = repo.findAll();

        return lista.stream().map(e -> mapper.map(e,Transaction.class)).collect(Collectors.toList());
    }

    // @GetMapping(value = "/pedidos/{cpf}", produces = "application/json;charset=UTF-8")
    // public List<PedidoDTO> listarPorCpf(@PathVariable("cpf") String cpf) {
    //     List<Pedido> lista = repo.findByCliente_Cpf(cpf);

    //     return lista.stream().map(e -> mapper.map(e,PedidoDTO.class)).collect(Collectors.toList());
    // }

    // @GetMapping(value = "/pedidos/produto/{id}", produces = "application/json;charset=UTF-8")
    // public List<PedidoDTO> listarPorIdProduto(@PathVariable("id") Integer id) {
    //     List<Pedido> lista = repo.findByItems_Produto_Id(id);

    //     return lista.stream().map(e -> mapper.map(e,PedidoDTO.class)).collect(Collectors.toList());
    // }

    // @PostMapping(value = "/pedidos/", produces = "application/json;charset=UTF-8")
    // public PedidoDTO inserir(@RequestBody PedidoDTO pedido) {
    //     Pedido novoPedido = new Pedido();
    //     novoPedido.setCliente(pedido.getCliente());
    //     novoPedido.setData(new Date());
    //     novoPedido.getItems().addAll(pedido.getItems()
    //         .stream()
    //         .map(item -> {
    //             Produto p = produtoService.findById(item.getProduto().getId()).get();
    //             ItemDoPedido i = new ItemDoPedido();
    //             i.setProduto(p);
    //             i.setPedido(novoPedido);
    //             i.setQuantidade(item.getQuantidade());
    //             return i;
    //         })
    //         .collect(Collectors.toList())
    //     );

    //     return mapper.map(repo.save(novoPedido), PedidoDTO.class);
    // }
}