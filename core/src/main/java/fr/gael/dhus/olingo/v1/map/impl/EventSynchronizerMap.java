/*
 * Data Hub Service(DHuS) - For Space data distribution.
 * Copyright(C) 2017 GAEL Systems
 *
 * This file is part of DHuS software sources.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or(at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package fr.gael.dhus.olingo.v1.map.impl;

import fr.gael.dhus.olingo.v1.entity.EventSynchronizer;
import fr.gael.dhus.olingo.v1.map.AbstractDelegatingMap;
import fr.gael.dhus.service.ISynchronizerService;
import fr.gael.dhus.spring.context.ApplicationContextProvider;

import java.util.Iterator;

import org.apache.olingo.odata2.api.exception.ODataException;

public class EventSynchronizerMap extends AbstractDelegatingMap<Long, EventSynchronizer>
{
   /** Synchronizer Service, the underlying service. */
   private static final ISynchronizerService SYNC_SERVICE =
         ApplicationContextProvider.getBean(ISynchronizerService.class);

   @Override
   protected EventSynchronizer serviceGet(Long key)
   {
      try
      {
         return new EventSynchronizer(SYNC_SERVICE.getSynchronizerConfById(key,
               fr.gael.dhus.database.object.config.synchronizer.EventSynchronizer.class));
      }
      catch (ODataException ex)
      {
         throw new RuntimeException(ex);
      }
   }

   @Override
   protected Iterator<EventSynchronizer> serviceIterator()
   {
      final Iterator<fr.gael.dhus.database.object.config.synchronizer.EventSynchronizer> it =
            SYNC_SERVICE.getSynchronizerConfs(fr.gael.dhus.database.object.config.synchronizer.EventSynchronizer.class);
      return new Iterator<EventSynchronizer>()
      {
         @Override
         public boolean hasNext()
         {
            return it.hasNext();
         }

         @Override
         public EventSynchronizer next()
         {
            try
            {
               return new EventSynchronizer(it.next());
            }
            catch (ODataException ex)
            {
               throw new RuntimeException(ex);
            }
         }

         @Override
         public void remove()
         {
            throw new UnsupportedOperationException();
         }
      };
   }

   @Override
   protected int serviceCount()
   {
      return SYNC_SERVICE.count();
   }
}
