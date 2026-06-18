package com._1.expert.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com._1.expert.dto.HttpGlobalResponse;
import com._1.expert.entity.Customers;
import com._1.expert.entity.Favorites;
import com._1.expert.entity.Products;
import com._1.expert.repository.CustomersRepository;
import com._1.expert.repository.FavoritesRepository;
import com._1.expert.repository.ProductsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoritesService {

    private final FavoritesRepository favoritesRepository;
    private final CustomersRepository customersRepository;
    private final ProductsRepository productsRepository;

    public HttpGlobalResponse<List<Favorites>> getAllFavorites() {

        HttpGlobalResponse<List<Favorites>> response = new HttpGlobalResponse<>();

        response.setMessage("Favoritos encontrados");
        response.setData(favoritesRepository.findAll());

        return response;
    }

    public HttpGlobalResponse<Object> getFavoriteById(Integer id) {

        HttpGlobalResponse<Object> response = new HttpGlobalResponse<>();

        Optional<Favorites> favorite = favoritesRepository.findById(id);

        if (favorite.isEmpty()) {
            response.setMessage("El favorito no existe");
            response.setData(null);
            return response;
        }

        response.setMessage("Favorito encontrado");
        response.setData(favorite.get());

        return response;
    }

    public HttpGlobalResponse<Object> saveFavorite(Integer customerId, Integer productId) {

        HttpGlobalResponse<Object> response = new HttpGlobalResponse<>();

        Optional<Customers> customer = customersRepository.findById(customerId);

        if (customer.isEmpty()) {
            response.setMessage("El cliente no existe");
            response.setData(null);
            return response;
        }

        Optional<Products> product = productsRepository.findById(productId);

        if (product.isEmpty()) {
            response.setMessage("El producto no existe");
            response.setData(null);
            return response;
        }

        Favorites favorite = new Favorites();
        favorite.setIdCustomer(customerId);
        favorite.setIdProduct(productId);

        Favorites saved = favoritesRepository.save(favorite);

        response.setMessage("Favorito creado correctamente");
        response.setData(saved);

        return response;
    }

    public HttpGlobalResponse<Object> updateFavorite(Integer id, Integer customerId, Integer productId) {

        HttpGlobalResponse<Object> response = new HttpGlobalResponse<>();

        Optional<Favorites> favorite = favoritesRepository.findById(id);

        if (favorite.isEmpty()) {
            response.setMessage("El favorito no existe");
            response.setData(null);
            return response;
        }

        Optional<Customers> customer = customersRepository.findById(customerId);

        if (customer.isEmpty()) {
            response.setMessage("El cliente no existe");
            response.setData(null);
            return response;
        }

        Optional<Products> product = productsRepository.findById(productId);

        if (product.isEmpty()) {
            response.setMessage("El producto no existe");
            response.setData(null);
            return response;
        }

        Favorites update = favorite.get();
        update.setIdCustomer(customerId);
        update.setIdProduct(productId);

        Favorites updated = favoritesRepository.save(update);

        response.setMessage("Favorito actualizado correctamente");
        response.setData(updated);

        return response;
    }

    public HttpGlobalResponse<Object> deleteFavorite(Integer id) {

        HttpGlobalResponse<Object> response = new HttpGlobalResponse<>();

        if (!favoritesRepository.existsById(id)) {
            response.setMessage("El favorito no existe");
            response.setData(null);
            return response;
        }

        favoritesRepository.deleteById(id);

        response.setMessage("Favorito eliminado correctamente");
        response.setData(null);

        return response;
    }
}
