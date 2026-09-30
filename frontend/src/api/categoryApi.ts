import type {Category, CategoryType ,CreateCategoryRequest} from "../types/category";

const API_BASE_URL = "http://localhost:8080"

interface ApiErrorResponse{
    message : string;
}

async function getErrorMessage(response:Response) : Promise<string> {
    let errBody = (await response.json().catch(()=>null)) as ApiErrorResponse | null;
    
    return errBody?.message ?? `请求失败，状态码：${response.status}`
}

export async function getCategories(type? : CategoryType,) : Promise<Category[]> {

    const url = new URL(`${API_BASE_URL}/get/category`)

    if(type){
        url.searchParams.set('type',type)
    }

    const response = await fetch(url)

    if(!response.ok){
        throw new Error(await getErrorMessage(response))
    }

    return response.json() as Promise<Category[]>
}

export async function createCategory(
  request: CreateCategoryRequest,
): Promise<Category> {
  const response = await fetch(`${API_BASE_URL}/get/category`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(request),
  });

  if (!response.ok) {
    throw new Error(await getErrorMessage(response));
  }

  return response.json() as Promise<Category>;
}

