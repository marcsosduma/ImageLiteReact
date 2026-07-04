import { Image } from './image.resource'
import { useAuthService } from '@/resources'

class ImageService {
    baseUrl: string = process.env.NEXT_PUBLIC_API_URL + '/v1/images';
    authService = useAuthService();

    async buscar(query: string = "", extension: string = "") : Promise<Image[]> {
        const userSession = this.authService.getUserSession();
        const url = `${this.baseUrl}?query=${query}&extension=${extension}`
        const response = await fetch(url, {
            headers: {
                'Authorization': `Bearer ${userSession?.accessToken}`
            }
        });
        return await response.json();
    }

    async salvar(dados: FormData) : Promise<string> {
        const userSession = this.authService.getUserSession();
        const response = await fetch(this.baseUrl, {
            method: 'POST',
            body: dados,
            headers: {
                'Authorization': `Bearer ${userSession?.accessToken}`
            }
        });
        return response.headers.get('location') ?? ''
    }
}

// react hook -> userState()
export const useImageService = () => new ImageService();