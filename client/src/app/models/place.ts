import { Review } from "./review";

export interface Place {
    address: string;
    name: string;
    placeId: string;
    rating: number;
    totalUsersRated: number;
    icon: string;
    iconColour: string;
    photoReference: string;
    latitude: number;
    longtitude: number;
    photo: string;
    website: string;
    review: Review[];

} 