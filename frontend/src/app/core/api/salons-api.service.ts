import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import {
  PageResponse,
  SalonDetails,
  SalonListItem,
  SalonSearchParams,
  UpdateSalonRequest,
} from '../../features/salons/models/salon.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SalonsApiService {
  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/salons';

  getSalons(params: SalonSearchParams = {}): Observable<PageResponse<SalonListItem>> {
    let httpParams = new HttpParams()
      .set('page', params.page ?? 0)
      .set('size', params.size ?? 20)
      .set('sortBy', params.sortBy ?? 'name')
      .set('direction', params.direction ?? 'asc');

    if (params.district) {
      httpParams = httpParams.set('district', params.district);
    }

    if (params.service) {
      httpParams = httpParams.set('service', params.service);
    }

    return this.http.get<PageResponse<SalonListItem>>(this.apiUrl, {
      params: httpParams,
    });
  }

  getSalon(id: number): Observable<SalonDetails> {
    return this.http.get<SalonDetails>(`${this.apiUrl}/${id}`);
  }

  updateSalon(id: number, request: UpdateSalonRequest): Observable<SalonDetails> {
    return this.http.patch<SalonDetails>(`${this.apiUrl}/${id}`, request);
  }

  getDistricts(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/districts`);
  }

  getServices(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/services`);
  }
}
