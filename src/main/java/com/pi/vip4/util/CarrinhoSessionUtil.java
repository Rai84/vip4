package com.pi.vip4.util;

import com.pi.vip4.model.Carrinho;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

public class CarrinhoSessionUtil {

    private static final String KEY = "carrinhoSession";

    @SuppressWarnings("unchecked")
    public static List<Carrinho> getCarrinho(HttpSession session) {
        List<Carrinho> carrinho = (List<Carrinho>) session.getAttribute(KEY);
        if (carrinho == null) {
            carrinho = new ArrayList<>();
            session.setAttribute(KEY, carrinho);
        }
        return carrinho;
    }

    public static void adicionar(HttpSession session, Carrinho item) {
        getCarrinho(session).add(item);
    }

    public static void limpar(HttpSession session) {
        session.removeAttribute(KEY);
    }
}
