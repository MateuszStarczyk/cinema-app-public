import { Movie } from 'src/app/shared/models/movie';
import { Screening } from 'src/app/admin/shared/models/screening';

export class MovieWithScreenings extends Movie {
    screenings: Screening[];
}
