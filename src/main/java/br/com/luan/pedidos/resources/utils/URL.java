package br.com.luan.pedidos.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//Esta classe vai ter a função de tratar as URLs, principalmente quando houver pesquisas com paginação, e que
//recebam parametros pelo URL que precisem ser convertidos para outros tipos
public class URL {

    //elimina os espaços da string, tem alguma coisa a ver com URL nao poder ter espaços e substituir por %.
    //o método decode de URLDecoder pode retornar uma exception, por isso sou obrigado a tratá-la.
    public static String decodeParam(String s){
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    //converte uma lista de categorias recebida como string em um objeto do tipo lista
    public static List<Integer> decodeIntList(String s) {
        String[] vetor = s.split(","); //split fatia a string baseado no caracte informado, neste caso, a virgula
        List<Integer> list = new ArrayList<>();
        for (int i=0; i<vetor.length; i++){
            list.add(Integer.parseInt(vetor[i]));
        }
        return list;

        //outra forma de fazer a mesma coisa é usando LAMBDA.
        //return Arrays.asList(s.split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
    }
}
