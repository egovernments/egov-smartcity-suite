package org.egov.infra.admin.common.service;

import org.egov.infra.admin.common.entity.Favourites;
import org.egov.infra.admin.common.repository.FavouritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class FavouritesService {
    
    @Autowired
    private FavouritesRepository favouritesRepository;

    public Favourites getFavouriteByUserIdAndActionId(Long userId, Integer actionId) {
        return favouritesRepository.findByUserIdAndActionId(userId, actionId);
    }
    
    @Transactional
    public void deleteFavourite(Favourites favourites) {
        favouritesRepository.delete(favourites);
    }
    
    @Transactional
    public Favourites createFavourite(Favourites favourites) {
        return favouritesRepository.save(favourites);
    }
}
