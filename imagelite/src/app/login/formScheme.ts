import * as Yup from 'yup';

export interface LoginForm {
    name?: string;
    email: string;
    password: string;
    passwordMatch?: string;
}

export const createValidationSchema = (newUserState: boolean) => {
    return Yup.object({
        name: Yup.string()
            .trim()
            .when([], {
                is: () => newUserState,
                then: (schema) => schema.required('Name is required'),
            }),

        email: Yup.string()
            .trim()
            .email('Invalid e-mail')
            .required('E-mail is required'),

        password: Yup.string()
            .trim()
            .min(6, 'Password must be at least 6 characters')
            .required('Password is required'),

        passwordMatch: Yup.string()
            .trim()
            .when([], {
                is: () => newUserState,
                then: (schema) =>
                    schema
                        .oneOf([Yup.ref('password')], 'Passwords must match')
                        .required('Password confirmation is required'),
            }),
    });
};

export const formScheme: LoginForm = {
    name: '',
    email: '',
    password: '',
    passwordMatch: '',
};